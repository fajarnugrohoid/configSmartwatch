package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class RoomChat implements MasterConstants {
    public int Id;
    public String Name;
    public int Src;
    public int Dst;
    public boolean Ismaster;

    public RoomChat(int roomchatid, String name, int src, int dst, int ismaster){
        this.Id = roomchatid;
        this.Name = name;
        this.Src = src;
        this.Dst = dst;
        this.Ismaster = ismaster == 1 ? true : false;
    }

    public RoomChat() {
        this(EMPTY_ID, "", 0, 0, 0);
    }

    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
