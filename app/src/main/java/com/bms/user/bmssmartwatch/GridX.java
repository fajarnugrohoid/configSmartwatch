package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class GridX implements MasterConstants {
    private final int Id;
    public int Hundreds;
    public int Gridx;
    public double Lon;

    public GridX(int id, int hundreds, int gridx, double lon) {
        this.Id = id;
        this.Hundreds = hundreds;
        this.Gridx = gridx;
        this.Lon = lon;
    }

    public GridX(){
        this(EMPTY_ID, 0, 0, 0);
    }

    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
