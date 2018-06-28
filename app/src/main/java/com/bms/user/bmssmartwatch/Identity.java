package com.bms.user.bmssmartwatch;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class Identity implements MasterConstants {
    private final int Id;
    public int Role;
    public String MmiIp;
    public String NiIp;
    public long ServerPort;
    public String Title;
    public String Descr;
    public String LinkType;
    public int SubtypeId;
    public String SubtypeName;
    public byte[] Image;
    public boolean HasCannon;

    public Identity(int id, int mRole, String mMmiIp, String mNiIp, long mServerPort, String mTitle, String mDescr, String mLinkType, int mSubtypeId, String mSubtypeName, byte[] mImage, int mHasCannon) {
        this.Id = id;
        this.Role = mRole;
        this.MmiIp = mMmiIp;
        this.NiIp = mNiIp;
        this.ServerPort = mServerPort;
        this.Title = mTitle;
        this.Descr = mDescr;
        this.LinkType = mLinkType;
        this.SubtypeId = mSubtypeId;
        this.SubtypeName = mSubtypeName;
        this.Image = mImage;
        this.HasCannon = mHasCannon == 1 ? true : false;
    }

    public Identity(){
        this(ONE, 1, "", "", 8019, "", "", "", 0, "", null, 0);
    }

    public Identity(int id, String mTitle, byte[] mImage, int mHasCannon, int mSubtypeId) {
        this(id, id, "", "", 8019, mTitle, "", "", mSubtypeId, "", mImage, mHasCannon);
    }

    public int getId() {
        return Id;
    }

    public static int EMPTY_ID(){
        return EMPTY_ID;
    }
}
