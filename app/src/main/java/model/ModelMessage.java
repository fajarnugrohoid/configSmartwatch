package model;

import android.graphics.PorterDuff;
import android.util.Log;

/**
 * Created by user on 07/05/2018.
 */


public class ModelMessage {
    private int idModelMessage;
    private String portName;
    private String addrId;
    private int FriendId;
    private Double Lat, Long;
    private String message;
    private static final String LOG_TAG = "smartwatch";

    public ModelMessage(int id,String portName,String addrId,int FriendId, Double Lat, Double Long, String message) {
        this.idModelMessage = id;
        this.portName = portName;
        this.addrId = addrId;
        this.FriendId = FriendId;
        this.Lat = Lat;
        this.Long = Long;
        this.message = message;
    }

    public int getId() {
        return idModelMessage;
    }

    public void setId(int id) {
        this.idModelMessage = id;
    }

    public String getPortName(){
        return  portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getAddrId(){
        return addrId;
    }

    public void setAddrId(String addrId){
        this.addrId = addrId;
    }

    public int getFriendId(){
        return FriendId;
    }

    public void setFriendId(int FrindId){
        this.FriendId = FriendId;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        this.Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        this.Long = aLong;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "message{ id:" + idModelMessage + ",message: " + message + "}";
    }
}
