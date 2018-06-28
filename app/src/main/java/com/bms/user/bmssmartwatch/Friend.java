package com.bms.user.bmssmartwatch;

import org.andnav.osm.util.GeoPoint;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class Friend implements MasterConstants {
    private final int Id;
    public int Address;
    public int ParentId;
    public int TypeId;
    public int SubtypeId;
    public byte[] Image;
    public String Title;
    public String Descr;
    public boolean Hidden;
    public double Fuel;
    public int Kanon;
    public int Gun;
    public double Temperature;
    public double Humidity;
    public org.andnav.osm.util.GeoPoint GeoPoint;
    public boolean HasCannon;
    public int Heading1;
    public int Heading2;
    public double Alt;
    public double Speed;
    public int Status;
    public String Lastupdate;
    public String Jenis;

    public Friend(int id, int address, int parentid, int typeid, int subtypeid, byte[] image, String title,
                  String descr, int hidden, double fuel, int kanon, int gun, double temperature,
                  double humidity, GeoPoint mGeoPoint, int hascannon, int heading1, int heading2, double alt,
                  double speed, int status, String lastupdate, String jenis) {
        this.Id = id;
        this.Address = address;
        this.ParentId = parentid;
        this.TypeId = typeid;
        this.SubtypeId = subtypeid;
        this.Image = image;
        this.Title = title;
        this.Descr = descr;
        this.Hidden = hidden == 1 ? true : false;
        this.Fuel = fuel;
        this.Kanon = kanon;
        this.Gun = gun;
        this.Temperature = temperature;
        this.Humidity = humidity;
        this.GeoPoint = mGeoPoint;
        this.HasCannon = hascannon == 1 ? true : false;
        this.Heading1 = heading1;
        this.Heading2 = heading2;
        this.Alt = alt;
        this.Speed = speed;
        this.Status = status;
        this.Lastupdate = lastupdate;
        this.Jenis = jenis;
    }

    public Friend(){
        this(EMPTY_ID, 0, 0, 0, 0, null, "", "", 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, 2, "", "");
    }

    public Friend(int id, int address, int parentid, String title, String descr, int typeid, int subtypeid, byte[] image) {
        this(id, address, parentid, typeid, subtypeid, image, title, descr, 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, 2, "", "");
    }

    public Friend(int id, int address, int parentid, String title, String descr, int hidden, int typeid, int subtypeid, byte[] image, GeoPoint mGeoPoint) {
        this(id, address, parentid,  typeid, subtypeid, image, title, descr, hidden, 0, 0, 0, 0, 0, mGeoPoint, 0, 0, 0, 0, 0, 2, "", "");
    }

    public Friend(int address, String title, String descr, GeoPoint mGeoPoint, byte[] image) {
        this(EMPTY_ID, address, 0, 0, 0, image, title, descr, 0, 0, 0, 0, 0, 0, mGeoPoint, 0, 0, 0, 0, 0, 2, "", "");
    }

    public Friend(int id, String title, String descr, GeoPoint mGeoPoint, int typeid, int subtypeid, byte[] image, int hascannon,
                  int bearing, int bearingc) {
        this(id, 0, 0, typeid, subtypeid, image, title, descr, 0, 0, 0, 0, 0, 0, mGeoPoint, hascannon, bearing, bearingc, 0, 0, 2, "", "");
    }

    public Friend(int address, String title, String descr, GeoPoint mGeoPoint) {
        this(EMPTY_ID, address, 0, 0, 0, null, title, descr, 0, 0, 0, 0, 0, 0, mGeoPoint, 0, 0, 0, 0, 0, 2, "", "");
    }

    public Friend(int address, int status) {
        this(EMPTY_ID, address, 0, 0, 0, null, "", "", 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, status, "", "");
    }

    public Friend(String title, String descr, byte[] image, int status, String subtype) {
        this(EMPTY_ID, 0, 0, 0, 0, image, title, descr, 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, status, "", subtype);
    }
    //add by AJ 19Nov2014 for kawan listview correction
    public Friend(int address,String title, String descr, byte[] image, int status, String subtype) {
        this(EMPTY_ID, address, 0, 0, 0, image, title, descr, 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, status, "", subtype);
        ;
    }

    public int getAddress() {
        return Address;
    }
    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
