package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class GridY implements MasterConstants {
    private final int Id;
    public int Hundreds;
    public int Gridy;
    public double Lat;

    public GridY(int id, int hundreds, int gridy, double lat) {
        this.Id = id;
        this.Hundreds = hundreds;
        this.Gridy = gridy;
        this.Lat = lat;
    }

    public GridY(){
        this(EMPTY_ID, 0, 0, 0);
    }

    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
