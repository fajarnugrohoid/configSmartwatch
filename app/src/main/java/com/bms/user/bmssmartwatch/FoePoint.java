package com.bms.user.bmssmartwatch;

import org.andnav.osm.util.GeoPoint;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class FoePoint implements MasterConstants {
    private final int Id;
    public String Type;
    public String Label;
    public org.andnav.osm.util.GeoPoint GeoPoint;
    public int SymbolId;
    public boolean Hidden;
    public boolean Active;
    public boolean Dead;

    public FoePoint(int id, String mType, String mLabel, GeoPoint mGeoPoint,
                    int symbolId, int hidden, int active, int dead) {
        this.Id = id;
        this.Type = mType;
        this.Label = mLabel;
        this.GeoPoint = mGeoPoint;
        this.SymbolId = symbolId;
        this.Hidden = hidden == 1 ? true : false;
        this.Active = active == 1 ? true : false;
        this.Dead = dead == 1 ? true : false;
    }

    public FoePoint(){
        this(EMPTY_ID, "", "", null, R.drawable.ic_action_location, 0, 0, 0);
    }

    public FoePoint(int id, String mType, String mLabel, GeoPoint mGeoPoint,
                    int symbolId) {
        this(id, mType, mLabel, mGeoPoint, symbolId, 0, 0, 0);
    }

    public FoePoint(String mTitle, String mLabel, GeoPoint mGeoPoint, int symbolId) {
        this(EMPTY_ID, mTitle, mLabel, mGeoPoint, symbolId, 0, 0, 0);
    }

    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
