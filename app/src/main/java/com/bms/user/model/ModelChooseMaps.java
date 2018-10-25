package com.bms.user.model;

public class ModelChooseMaps {
    private int idx;
    private String mapsName;
    private String mapsPath;

    public ModelChooseMaps(){
        this.idx = 0;
        this.mapsName = "";
        this.mapsPath = "";
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getMapsName() {
        return mapsName;
    }

    public void setMapsName(String mapsName) {
        this.mapsName = mapsName;
    }

    public String getMapsPath() {
        return mapsPath;
    }

    public void setMapsPath(String mapsPath) {
        this.mapsPath = mapsPath;
    }
}
