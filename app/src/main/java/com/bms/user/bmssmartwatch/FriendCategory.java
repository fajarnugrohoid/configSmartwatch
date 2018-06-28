package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class FriendCategory implements MasterConstants {
    private int Id;
    public String Title;
    public boolean Hidden;
    public int IconId;
    public int MinZoom;

    public FriendCategory(int id, String title, boolean hidden, int iconid, int minzoom) {
        super();
        Id = id;
        Title = title;
        Hidden = hidden;
        IconId = iconid;
        MinZoom = minzoom;
    }

    public FriendCategory() {

        //this(MasterConstants.EMPTY_ID, "", false, R.drawable.poi, 14);
    }

    public FriendCategory(String title) {
        //this(MasterConstants.EMPTY_ID, title, false, R.drawable.poi, 14);
    }

    public int getId() {
        return Id;
    }
}
