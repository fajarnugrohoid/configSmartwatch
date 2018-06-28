package com.bms.user.bmssmartwatch;

import android.location.Location;

import org.andnav.osm.util.GeoPoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class Skenario implements MasterConstants {
    public static final String COLOR = "color";
    public static final String COLORSHADOW = "color_shadow";
    public static final String WIDTH = "width";
    public static final String SHADOWRADIUS = "shadowradius";

    private int Id;
    public String Name;
    public String Descr;
    public SkenarioPoint LastSkenarioPoint;
    public boolean Show;
    public int Cnt;
    public double Distance;
    public double Duration;
    public int Category;
    public int Activity;
    public java.util.Date Date;
    public int Color;
    public int ColorShadow;
    public int Width;
    public double ShadowRadius;
    public String Style;
    public String DefaultStyle;
    public ArrayList<GeoPoint> gPoints = new ArrayList<GeoPoint>();
    //Add 190416
    public int Skenariosrcid;

    private List<SkenarioPoint> skenariopoints = null;

    public class SkenarioPoint {
        public double lat;
        public double lon;
        public double alt;
        public double speed;
        public Date date;

        public SkenarioPoint() {
            lat = 0;
            lon = 0;
            alt = 0;
            speed = 0;
            date = new Date();
        }

        public int getLatitudeE6() {
            return (int) (lat * 1E6);
        }

        public int getLongitudeE6() {
            return (int) (lon * 1E6);
        }
    };

    public List<SkenarioPoint> getPoints(){
        if(skenariopoints == null)
            return new ArrayList<SkenarioPoint>(0);

        return skenariopoints;
    }

    public void AddSkenarioPoint(){
        LastSkenarioPoint = new SkenarioPoint();
        if(skenariopoints == null)
            skenariopoints = new ArrayList<SkenarioPoint>(1);
        skenariopoints.add(LastSkenarioPoint);
    }

    public Skenario() {
        //this(EMPTY_ID, "", "", false, 0, 0, 0, 0, 0, new Date(0), "", "");
        this(EMPTY_ID, "", "", false, 0, 0, 0, 0, 0, new Date(0), "", "", 0);
    }

    public Skenario(final String style) {
        //this(EMPTY_ID, "", "", false, 0, 0, 0, 0, 0, new Date(0), style, style);
        this(EMPTY_ID, "", "", false, 0, 0, 0, 0, 0, new Date(0), style, style, 0);
    }

    //public Skenario(final int id, final String name, final String descr, final boolean show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style, final String defaultStyle) {
    public Skenario(final int id, final String name, final String descr, final boolean show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style, final String defaultStyle, final int skenariosrcid) {
        Id = id;
        Name = name;
        Descr = descr;
        Show = show;
        Cnt = cnt;
        Distance = distance;
        Duration = duration;
        Category = category;
        Activity = activity;
        Date = date;
        Style = style;
        DefaultStyle = defaultStyle;
        Skenariosrcid = skenariosrcid;

        try {
            final JSONObject json = new JSONObject(Style.equalsIgnoreCase("") ? DefaultStyle : Style);
            Color = json.optInt(COLOR, 0xffA565FE);
            Width = json.optInt(WIDTH, 4);
            ShadowRadius = json.optDouble(SHADOWRADIUS, 0);
            ColorShadow = json.optInt(COLORSHADOW, 0xffA565FE);
        } catch (Exception e) {
            Color = 0xffA565FE;
            Width = 4;
            ShadowRadius = 0;
            ColorShadow = 0xffA565FE;
        }
    }

    public String getStyle() {
        final JSONObject json = new JSONObject();
        try {
            json.put(COLOR, Color);
            json.put(COLORSHADOW, ColorShadow);
            json.put(WIDTH, Width);
            json.put(SHADOWRADIUS, ShadowRadius);
        } catch (JSONException e) {
        }
        return json.toString();
    }

    public int getId() {
        return Id;
    }

    public void setId(int mskenarioId) {
        this.Id = mskenarioId;
    }

    public int getSkenaId(){
        return Skenariosrcid;
    }

    public void setSkenaId(int mskenariosrcId){
        this.Skenariosrcid = mskenariosrcId;
    }

    public GeoPoint getBeginGeoPoint() {
        if(skenariopoints.size()>0)
            return new GeoPoint(skenariopoints.get(0).getLatitudeE6(), skenariopoints.get(0).getLongitudeE6());
        return null;
    }

    public void CalculateStat() {
        Cnt = skenariopoints.size();
        Duration = 0;
        if (skenariopoints.size() > 0)
            Duration = (double) ((skenariopoints.get(skenariopoints.size() - 1).date.getTime() - skenariopoints.get(0).date.getTime())/1000);
        SkenarioPoint lastpt = null;
        Distance = 0;
        float[] results = {0};

        for(SkenarioPoint pt : skenariopoints){
            if(lastpt != null){
                results[0] = 0;
                try {
                    Location.distanceBetween(lastpt.lat, lastpt.lon, pt.lat, pt.lon, results);
                    Distance += results[0];
                } catch (Exception e) {
                }
            }
            lastpt = pt;
        }
    }

    public class Stat{
        public Date Date1;
        public Date Date2;
        public double MaxSpeed;
        public double AvgSpeed;
        public double AvgPace;
        public double MinEle;
        public double MaxEle;
        public int MoveTime;
        public double AvgMoveSpeed;
    };

    public Stat CalculateStatFull() {
        SkenarioPoint lastpt = null;
        final Stat stat = new Stat();

        if(Duration > 0)
            stat.AvgSpeed = (Distance / 1000) / (Duration/60/60);
        if(Distance > 0)
            stat.AvgPace = Duration / (Distance / 1000);

        for(SkenarioPoint pt : skenariopoints){
            if(lastpt == null){
                stat.Date1 = pt.date;
                stat.MaxSpeed = 0.0;
                stat.MinEle = pt.alt;
                stat.MaxEle = pt.alt;
            } else {
                if(pt.speed > stat.MaxSpeed)
                    stat.MaxSpeed = pt.speed;
                if(pt.alt > stat.MaxEle)
                    stat.MaxEle = pt.alt;
                if(pt.alt < stat.MinEle)
                    stat.MinEle = pt.alt;
                if(lastpt.speed > 0.5)
                    stat.MoveTime += pt.date.getTime() - lastpt.date.getTime();

            }
            lastpt = pt;
        }
        if (lastpt != null)
            stat.Date2 = lastpt.date;
        if(stat.MoveTime > 0){
            stat.AvgMoveSpeed = (Distance / 1000) / (stat.MoveTime/1000/60.0/60.0);
        }

        stat.MaxSpeed *= 3.6;

        return stat;
    }
}
