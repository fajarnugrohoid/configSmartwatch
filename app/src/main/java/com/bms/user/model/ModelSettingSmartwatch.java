package com.bms.user.model;

public class ModelSettingSmartwatch {
    private int idx;
    private long smartwatchId;
    private String sendTo;
    private String mapsName;
    private String mapsPath;
    private String group;
    private String ipCCU;
    private String ipDriverView;
    private String imageFileNameEnding;
    private int minZoomLvl;
    private int maxZoomLvl;
    private int firstZoomLvl;
    private int tileSizePixel;
    private String sdrLat;
    private String sdrLon;
    private Integer isCompassExternal; //use compass external(ccu) = 1 -> true, 0 : false

    public ModelSettingSmartwatch(){
        this.idx = 0;
        this.smartwatchId = 0;
        this.mapsName = "";
        this.mapsPath = "";
        this.group = "0";
        this.ipCCU = "";
        this.ipDriverView = "";
        this.imageFileNameEnding = "";
        this.minZoomLvl=0;
        this.maxZoomLvl=0;
        this.firstZoomLvl=0;
        this.tileSizePixel=0;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public long getSmartwatchId() {
        return smartwatchId;
    }

    public void setSmartwatchId(long smartwatchId) {
        this.smartwatchId = smartwatchId;
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

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIpCCU() {
        return ipCCU;
    }

    public void setIpCCU(String ipCCU) {
        this.ipCCU = ipCCU;
    }

    public String getIpDriverView() {
        return ipDriverView;
    }

    public void setIpDriverView(String ipDriverView) {
        this.ipDriverView = ipDriverView;
    }

    public String getImageFileNameEnding() {
        return imageFileNameEnding;
    }

    public void setImageFileNameEnding(String imageFileNameEnding) {
        this.imageFileNameEnding = imageFileNameEnding;
    }

    public int getMinZoomLvl() {
        return minZoomLvl;
    }

    public void setMinZoomLvl(int minZoomLvl) {
        this.minZoomLvl = minZoomLvl;
    }

    public int getMaxZoomLvl() {
        return maxZoomLvl;
    }

    public void setMaxZoomLvl(int maxZoomLvl) {
        this.maxZoomLvl = maxZoomLvl;
    }

    public int getFirstZoomLvl() {
        return firstZoomLvl;
    }

    public void setFirstZoomLvl(int firstZoomLvl) {
        this.firstZoomLvl = firstZoomLvl;
    }

    public int getTileSizePixel() {
        return tileSizePixel;
    }

    public void setTileSizePixel(int tileSizePixel) {
        this.tileSizePixel = tileSizePixel;
    }

    public String getSdrLat() {
        return sdrLat;
    }

    public void setSdrLat(String sdrLat) {
        this.sdrLat = sdrLat;
    }

    public String getSdrLon() {
        return sdrLon;
    }

    public void setSdrLon(String sdrLon) {
        this.sdrLon = sdrLon;
    }

    public Integer getIsCompassExternal() {
        return isCompassExternal;
    }

    public void setIsCompassExternal(Integer isCompassExternal) {
        this.isCompassExternal = isCompassExternal;
    }
}
