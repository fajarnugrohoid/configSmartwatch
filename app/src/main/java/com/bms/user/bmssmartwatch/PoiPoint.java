package com.bms.user.bmssmartwatch;

import org.andnav.osm.util.GeoPoint;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class PoiPoint implements MasterConstants {
    private int Id;
    public String Title;
    public String Descr;
    public org.andnav.osm.util.GeoPoint GeoPoint;
    public int IconId;
    public double Alt;
    public int CategoryId;
    public int PointSourceId;
    public boolean Hidden;

    public PoiPoint(int id, String mTitle, String mDescr, GeoPoint mGeoPoint,
                    int iconid, int categoryid, double alt, int sourseid, int hidden) {
        this.Id = id;
        this.Title = mTitle;
        this.Descr = mDescr;
        this.GeoPoint = mGeoPoint;
        this.IconId = iconid;
        this.Alt = alt;
        this.CategoryId = categoryid;
        this.PointSourceId = sourseid;
        this.Hidden = hidden == 1 ? true : false;
    }

    public PoiPoint(){
        this(EMPTY_ID, "", "", null, 90, 0, 0, 0, 0);
    }

    public PoiPoint(int id, String mTitle, String mDescr, GeoPoint mGeoPoint,
                    int categoryid, int iconid) {
        this(id, mTitle, mDescr, mGeoPoint, iconid, categoryid, 0, 0, 0);
    }

    public PoiPoint(String mTitle, String mDescr, GeoPoint mGeoPoint, int iconid) {
        this(EMPTY_ID, mTitle, mDescr, mGeoPoint, iconid, 0, 0, 0, 0);
    }

    public int getId() {
        return Id;
    }

    public int getPointSrcId() {
        return PointSourceId;
    }

    public void setId(int mId) {
        this.Id = mId;
    }

    public void setPointSrcId(int mPointSrcId) {
        this.PointSourceId = mPointSrcId;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
