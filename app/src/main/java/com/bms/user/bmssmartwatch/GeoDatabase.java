package com.bms.user.bmssmartwatch;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import utils.RSQLiteOpenHelper;
import utils.Ut;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class GeoDatabase implements MasterConstants {
    protected final Context mCtx;
    private SQLiteDatabase mDatabase;
    protected final SimpleDateFormat DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private String TAG = "GeoDatabase";

    public GeoDatabase(Context ctx) {
        super();
        mCtx = ctx;
        mDatabase = getDatabase();
    }

    public Cursor getChatListCursor(final int roomchatid) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(roomchatid)};

            return mDatabase.rawQuery(STAT_ChatByRoomchatid, args);
        }

        return null;
    }

    public Cursor getChatListCursor_10() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_Chat_10, null);
        }

        return null;
    }

    public long addChat(final int aRoomchatid, final String aSender, final String aReceiver, final String aSenddate, final String aReceivedate, int aIsread, String aMsg , String aStatus) {
        long newId = -1;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(ROOMCHATID, aRoomchatid);
            cv.put(SENDER, aSender);
            cv.put(RECEIVER, aReceiver);
            cv.put(SDATE, aSenddate);
            cv.put(RDATE, aReceivedate);
            cv.put(ISREAD, aIsread);
            cv.put(MSG, aMsg);
            cv.put(STATUS, aStatus);
            newId = this.mDatabase.insert(CHAT, null, cv);
        }

        return newId;
    }

    public long addChatAck(final String aRoomchatid, final String aSender, final int aReceiver, final String aReceivedate, String aMsg) {
        long newId = -1;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(ROOMCHATID2, aRoomchatid);
            cv.put(SENDER2, aSender);
            cv.put(RECEIVER2, aReceiver);
            cv.put(RDATE2, aReceivedate);
            cv.put(MSG2, aMsg);
            newId = this.mDatabase.insert(CHATACK, null, cv);
        }

        return newId;
    }

    public void updateChat(final int id, final int aRoomchatid, final String aSender, final String aReceiver, final String aSenddate, final String aReceivedate, int aIsread, String aMsg) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(ROOMCHATID, aRoomchatid);
            cv.put(SENDER, aSender);
            cv.put(RECEIVER, aReceiver);
            cv.put(SDATE, aSenddate);
            cv.put(RDATE, aReceivedate);
            cv.put(ISREAD, aIsread);
            cv.put(MSG, aMsg);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(CHAT, cv, UPDATE_CHAT, args);
        }
    }



    public void updateChatRcvDate(final String aSenddate, final String aReceivedate) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(RDATE, aReceivedate);
            final String[] args = {aSenddate};
            this.mDatabase.update(CHAT, cv, UPDATE_CHAT_SEND_DATE, args);
        }
    }

    public void updateChatIsRead(int roomchatid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("isread", "1");
            final String[] args = {Integer.toString(roomchatid)};
            this.mDatabase.update(CHAT, cv, "roomchatid = @1", args);
        }
    }

    public Cursor getRoomchatListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_ROOMCHAT_LIST, null);
        }
        return null;
    }

    public Cursor getRoomChat(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};

            return mDatabase.rawQuery(STAT_getRoomChat, args);
        }

        return null;
    }

    @SuppressLint("NewApi")
    public int countUnreadSms(int aRoomChatId) {
        if (isDatabaseReady()) {
            return (int) DatabaseUtils.queryNumEntries(mDatabase, "chat", "roomchatid="+aRoomChatId+" AND isread=0");
        }

        return 0;
    }

    public void clearChat(final int roomchatid) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(roomchatid)};
            mDatabase.execSQL(STAT_deleteChat, args);
        }
    }

    public void addRoomchat(final int aRoomchatid, final String aName, final int aSrc, final int aDst, final int aIsmaster) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(ROOMCHATID, aRoomchatid);
            cv.put(NAME, aName);
            cv.put(SRC, aSrc);
            cv.put(DST, aDst);
            cv.put(ISMASTER, aIsmaster);
            this.mDatabase.insert(ROOMCHAT, null, cv);
        }
    }

    public int updateRoomchat(final int aRoomchatid, final String aName, final int aSrc, final int aDst, final int aIsmaster) {
        int res = 0;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, aName);
            cv.put(SRC, aSrc);
            cv.put(DST, aDst);
            cv.put(ISMASTER, aIsmaster);

            final String[] args = {Integer.toString(aRoomchatid)};
            res = this.mDatabase.update(ROOMCHAT, cv, UPDATE_ROOMCHAT, args);
        }

        return res;
    }

    public void deleteRoomchat(final int roomchatid) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(roomchatid)};
            mDatabase.execSQL(STAT_deleteRoomchat, args);
        }
    }

    public long addPoi(final String aName, final String aDescr, final double aLat, final double aLon, final double aAlt, final int aCategoryId,
                       final int aPointSourceId, final int hidden, final int iconid) {
        long newId = -1;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, aName);
            cv.put(DESCR, aDescr);
            cv.put(LAT, aLat);
            cv.put(LON, aLon);
            cv.put(ALT, aAlt);
            cv.put(CATEGORYID, aCategoryId);
            cv.put(POINTSOURCEID, aPointSourceId);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            newId = this.mDatabase.insert(POINTS, null, cv);

        }
        return newId;
    }

    public long addPoiWithId(final int id, final String aName, final String aDescr, final double aLat, final double aLon, final double aAlt, final int aCategoryId,
                             final int aPointSourceId, final int hidden, final int iconid) {
        long newId = -1;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pointid", id);
            cv.put(NAME, aName);
            cv.put(DESCR, aDescr);
            cv.put(LAT, aLat);
            cv.put(LON, aLon);
            cv.put(ALT, aAlt);
            cv.put(CATEGORYID, aCategoryId);
            cv.put(POINTSOURCEID, aPointSourceId);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            newId = this.mDatabase.insert(POINTS, null, cv);
        }
        return newId;
    }

    public void updatePoi(final int id, final String aName, final String aDescr, final double aLat, final double aLon, final double aAlt, final int aCategoryId,
                          final int aPointSourceId, final int hidden, final int iconid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, aName);
            cv.put(DESCR, aDescr);
            cv.put(LAT, aLat);
            cv.put(LON, aLon);
            cv.put(ALT, aAlt);
            cv.put(CATEGORYID, aCategoryId);
            cv.put(POINTSOURCEID, aPointSourceId);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(POINTS, cv, UPDATE_POINTS, args);
        }
    }

    public void updatePoiId(final int id, final int newId, final String aName, final String aDescr, final double aLat, final double aLon, final double aAlt, final int aCategoryId,
                            final int aPointSourceId, final int hidden, final int iconid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pointid", newId);
            cv.put(NAME, aName);
            cv.put(DESCR, aDescr);
            cv.put(LAT, aLat);
            cv.put(LON, aLon);
            cv.put(ALT, aAlt);
            cv.put(CATEGORYID, aCategoryId);
            cv.put(POINTSOURCEID, aPointSourceId);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(POINTS, cv, UPDATE_POINTS, args);
        }
    }

    public int countObjectId(final int objectId) {
        if (isDatabaseReady()) {
            return (int) DatabaseUtils.queryNumEntries(mDatabase, "points", "pointid="+objectId);
        }

        return 0;
    }

    public int countObjectSrcId(final int SrcObjectId) {
        if (isDatabaseReady()) {
            return (int) DatabaseUtils.queryNumEntries(mDatabase, "points", "pointsourceid="+SrcObjectId);
        }

        return 0;
    }

    public int countSkenarioId(final int skenarioId) {
        if (isDatabaseReady()) {
            return (int) DatabaseUtils.queryNumEntries(mDatabase, SKENARIO, "pskenarioid="+skenarioId);
        }

        return 0;
    }

    public int countSkenarioSrcId(final int skenariosrcid) {
        if (isDatabaseReady()) {
            return (int) DatabaseUtils.queryNumEntries(mDatabase, SKENARIO, "skenariosrcid="+skenariosrcid);
        }

        return 0;
    }

    public void updateSymbolMsh(final int id, final int symboId) {
        if (isDatabaseReady()) {
            final Integer[] args = {id, symboId};
            mDatabase.execSQL(STAT_updateSymbolMsh, args);
        }
    }

    public void addType(final int id, String name) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("typeid", id);
            cv.put("name", name);
            this.mDatabase.insert("type", null, cv);
        }
    }

    public void addSubType(final int id, int typeid, String name, byte[] image, int hascannon) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("subtypeid", id);
            cv.put("typeid", typeid);
            cv.put("name", name);
            cv.put("image", image);
            cv.put("hascannon", hascannon);
            this.mDatabase.insert("subtype", null, cv);
        }
    }

    //New 2811 FTP
    public void addFtp(final int id, String name, int status){
        if (isDatabaseReady()){
            final ContentValues cv = new ContentValues();
            cv.put("fileid", id);
            cv.put("filename", name);
            cv.put("status", status);
            this.mDatabase.insert("ftp", null, cv);
            Toast.makeText(mCtx, "FTP", Toast.LENGTH_LONG).show();
        }
    }

    public void addCuaca(final int id, double suhu, double kelembaban, double hujan, double kabut, double angin){
        if (isDatabaseReady()){
            final ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("suhu", suhu);
            cv.put("kelembaban", kelembaban);
            cv.put("hujan", hujan);
            cv.put("kabut", kabut);
            cv.put("angin", angin);
            this.mDatabase.insert("cuaca", null, cv);
        }
    }

    public void updateCuaca(final int id, double suhu, double kelembaban, double hujan, double kabut, double angin){
        if (isDatabaseReady()){
            final ContentValues cv = new ContentValues();
            cv.put("suhu", suhu);
            cv.put("kelembaban", kelembaban);
            cv.put("hujan", hujan);
            cv.put("kabut", kabut);
            cv.put("angin", angin);
            final String[] args = {Integer.toString(id)};
            //this.mDatabase.update("cuaca", cv, UPDATE_CUACA, args);
            this.mDatabase.update("cuaca", cv, UPDATE_CUACA, args);
        }
    }

    public void deleteAllCuaca() {
        // TODO Auto-generated method stub
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllCuaca);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if(mDatabase != null){
            if(mDatabase.isOpen()){
                mDatabase.close();
                mDatabase = null;
            }
        }
        super.finalize();
    }

    public Cursor getPoiListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_POI_LIST, null);
        }

        return null;
    }

    public Cursor getFoeListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_FOE_LIST, null);
        }

        return null;
    }

    public Cursor getMyIdentityCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_MYIDENTITY, null);
        }

        return null;
    }

    public Cursor getCuacaCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_CUACA, null);
        }

        return null;
    }

    public Cursor getPoiCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_POI, null);
        }

        return null;
    }

    public Cursor getMyOverlayCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_MYOVERLAY, null);
        }

        return null;
    }

    public Cursor getTrackPointCursor() {
        if(isDatabaseReady()) {
            return mDatabase.rawQuery("SELECT * FROM trackpoints", null);
        }

        return null;
    }

    public Cursor getPoiListNotHiddenCursor(final int zoom, final double left, final double right, final double top, final double bottom) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(zoom + 1),Double.toString(left),Double.toString(right),Double.toString(bottom),Double.toString(top)};

            return mDatabase.rawQuery(STAT_PoiListNotHidden, args);
        }

        return null;
    }

    public Cursor getGridxList(final double left, final double right, final double top, final double bottom) {
        if (isDatabaseReady()) {
            final String[] args = {Double.toString(left),Double.toString(right)};

            return mDatabase.rawQuery(STAT_GridxList, args);
        }

        return null;
    }

    public Cursor getGridyList(final double left, final double right, final double top, final double bottom) {
        if (isDatabaseReady()) {
            final String[] args = {Double.toString(bottom),Double.toString(top)};

            return mDatabase.rawQuery(STAT_GridyList, args);
        }

        return null;
    }

    public Cursor getPoiCategoryListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_PoiCategoryList, null);
        }

        return null;
    }

    public Cursor getActivityListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_ActivityList, null);
        }

        return null;
    }

    public Cursor getPoi(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};

            return mDatabase.rawQuery(STAT_getPoi, args);
        }

        return null;
    }

    public Cursor getPoiSrcId(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};

            return mDatabase.rawQuery(STAT_getPoiSrcId, args);
        }

        return null;
    }

    public Cursor getSknSrcId(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};

            return mDatabase.rawQuery(STAT_getSknSrcId, args);
        }

        return null;
    }

    public void deletePoi(final int id) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(id)};
            mDatabase.execSQL(STAT_deletePoi, args);
        }
    }

    public void deletePoiSrcId(final int SrcId) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(SrcId)};
            mDatabase.execSQL(STAT_deletePoiSrcId, args);
        }
    }
    public void deletePoiSrcId2(final int SrcId) {
        if (isDatabaseReady()) {

            final Double[] args = {Double.valueOf(SrcId)};

            mDatabase.execSQL("DELETE FROM points WHERE pointid =="+SrcId+"");
            //DELETE FROM points WHERE pointsourceid =
        }
        mDatabase.execSQL("DELETE FROM points WHERE pointid =="+SrcId+"");

    }

    public void deleteSknSrcId(final int SrcId) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(SrcId)};
            mDatabase.execSQL(STAT_deletePoiSrcId, args);
        }
    }

    public void deletePoiCategory(final int id) {
        if (isDatabaseReady() && id != ZERO) { // predef category My POI never delete
            final Double[] args = {Double.valueOf(id)};
            mDatabase.execSQL(STAT_deletePoiCategory, args);
        }
    }

    private boolean isDatabaseReady() {
        boolean ret = true;

        if(mDatabase == null)
            mDatabase = getDatabase();

        if(mDatabase == null)
            ret = false;
        else if(!mDatabase.isOpen())
            mDatabase = getDatabase();

        if(ret == false)
            try {
                //Toast.makeText(mCtx, mCtx.getText(R.string.message_geodata_notavailable), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return ret;
    }

    public void FreeDatabases(){
        if(mDatabase != null){
            if(mDatabase.isOpen()){
                mDatabase.close();
            }
            mDatabase = null;
        }
    }

    protected SQLiteDatabase getDatabase() {
        File folder = Ut.getBMSMainDir(mCtx, DATA);
        if(!folder.exists()) // no sdcard
            return null;

        SQLiteDatabase db;
        try {
            db = new GeoDatabaseHelper(mCtx, folder.getAbsolutePath() + GEODATA_FILENAME).getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.i(TAG,"DB Version: "+db.getVersion());
        return db;
    }

    public void insertKendali() {
        mDatabase.execSQL("INSERT INTO kendali VALUES (1, 'Kendali Tempur 1', '', null, 1, 0, 0, 0, 0, 0, '');");
    }

    public void insertKendaliPoints() {
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 1, -6.946396, 107.613131, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 2, -6.947196, 107.613534, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 3, -6.945852, 107.615943, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 4, -6.946707, 107.61638, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 5, -6.94461, 107.617195, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 6, -6.944653, 107.615092, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 7, -6.945285, 107.615573, 0, 0, null);");
        mDatabase.execSQL("INSERT INTO kendalipoints VALUES (1, 8, -6.946396, 107.613131, 0, 0, null);");
    }

    public void insertSkenarioPoints() {
        mDatabase.execSQL("insert into pskenariopoints values (1, 7, -6.948111, 107.605115, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 8, -6.948452, 107.609342, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 9, -6.949218, 107.619342, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 10, -6.949559, 107.625285, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 11, -6.949069, 107.629427, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 12, -6.94794, 107.633418, 0, 0, 1365514518);");
        mDatabase.execSQL("insert into pskenariopoints values (1, 13, -6.942701, 107.628247, 0, 0, 1365514518);");
    }

    protected class GeoDatabaseHelper extends RSQLiteOpenHelper {
        private final static int mCurrentVersion = 58;

        public GeoDatabaseHelper(final Context context, final String name) {
            super(context, name, null, mCurrentVersion);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            db.execSQL(MasterConstants.SQL_CREATE_points);
            db.execSQL(MasterConstants.SQL_CREATE_pointsource);
            db.execSQL(MasterConstants.SQL_CREATE_category);
            db.execSQL(MasterConstants.SQL_ADD_category);
            db.execSQL(MasterConstants.SQL_CREATE_tracks);
            db.execSQL(MasterConstants.SQL_CREATE_trackpoints);
            db.execSQL(MasterConstants.SQL_CREATE_maps);
            db.execSQL(MasterConstants.SQL_CREATE_friends_5);
            db.execSQL(MasterConstants.SQL_CREATE_foes);
            db.execSQL(MasterConstants.SQL_CREATE_kendali);
            db.execSQL(MasterConstants.SQL_CREATE_kendalipoints);
            db.execSQL(MasterConstants.SQL_CREATE_psituasi);
            db.execSQL(MasterConstants.SQL_CREATE_pskenario);
            db.execSQL(MasterConstants.SQL_CREATE_pskenariopoints);
            db.execSQL(MasterConstants.SQL_CREATE_smanuver);
            db.execSQL(MasterConstants.SQL_CREATE_smanuverpoints);
            db.execSQL(MasterConstants.SQL_CREATE_smarka);
            db.execSQL(MasterConstants.SQL_CREATE_smarkapoints);
            db.execSQL(MasterConstants.SQL_CREATE_sobyek);
            db.execSQL(MasterConstants.SQL_CREATE_cuaca);
            db.execSQL(MasterConstants.SQL_CREATE_chatack);
            db.execSQL(MasterConstants.SQL_CREATE_chat);
            db.execSQL(MasterConstants.SQL_CREATE_roomchat);
            db.execSQL(MasterConstants.SQL_CREATE_tactical_symbol);
            db.execSQL(MasterConstants.SQL_CREATE_identity);
            db.execSQL(MasterConstants.SQL_CREATE_manual_ops);
            db.execSQL(MasterConstants.INS_DEFAULT_IDENTITY);
            db.execSQL(MasterConstants.INS_DEFAULT_MANUAL_OPS);
            db.execSQL(MasterConstants.SQL_CREATE_type);
            db.execSQL(MasterConstants.SQL_CREATE_subtype);
            LoadActivityListFromResource(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
//			Ut.dd("Upgrade data.db from ver." + oldVersion + " to ver."
//					+ newVersion);

            if (oldVersion < 2) {
                db.execSQL(MasterConstants.SQL_UPDATE_1_1);
                db.execSQL(MasterConstants.SQL_UPDATE_1_2);
                db.execSQL(MasterConstants.SQL_UPDATE_1_3);
                db.execSQL(MasterConstants.SQL_CREATE_points);
                db.execSQL(MasterConstants.SQL_UPDATE_1_5);
                db.execSQL(MasterConstants.SQL_UPDATE_1_6);
                db.execSQL(MasterConstants.SQL_UPDATE_1_7);
                db.execSQL(MasterConstants.SQL_UPDATE_1_8);
                db.execSQL(MasterConstants.SQL_UPDATE_1_9);
                db.execSQL(MasterConstants.SQL_CREATE_category);
                db.execSQL(MasterConstants.SQL_ADD_category);
                //db.execSQL(MasterConstants.SQL_UPDATE_1_11);
                //db.execSQL(MasterConstants.SQL_UPDATE_1_12);
            }
            if (oldVersion < 3) {
                db.execSQL(MasterConstants.SQL_UPDATE_2_7);
                db.execSQL(MasterConstants.SQL_UPDATE_2_8);
                db.execSQL(MasterConstants.SQL_UPDATE_2_9);
                db.execSQL(MasterConstants.SQL_CREATE_category);
                db.execSQL(MasterConstants.SQL_UPDATE_2_11);
                db.execSQL(MasterConstants.SQL_UPDATE_2_12);
            }
            if (oldVersion < 5) {
                db.execSQL(MasterConstants.SQL_CREATE_tracks);
                db.execSQL(MasterConstants.SQL_CREATE_trackpoints);
            }
            if (oldVersion < 18) {
                db.execSQL(MasterConstants.SQL_UPDATE_6_1);
                db.execSQL(MasterConstants.SQL_UPDATE_6_2);
                db.execSQL(MasterConstants.SQL_UPDATE_6_3);
                db.execSQL(MasterConstants.SQL_CREATE_tracks);
                db.execSQL(MasterConstants.SQL_UPDATE_6_4);
                db.execSQL(MasterConstants.SQL_UPDATE_6_5);
                LoadActivityListFromResource(db);
            }
            if (oldVersion < 20) {
                db.execSQL(MasterConstants.SQL_UPDATE_6_1);
                db.execSQL(MasterConstants.SQL_UPDATE_6_2);
                db.execSQL(MasterConstants.SQL_UPDATE_6_3);
                db.execSQL(MasterConstants.SQL_CREATE_tracks);
                db.execSQL(MasterConstants.SQL_UPDATE_20_1);
                db.execSQL(MasterConstants.SQL_UPDATE_6_5);
            }
            if (oldVersion < 26) {
                db.execSQL(MasterConstants.SQL_CREATE_foes);
            }
            if (oldVersion < 29) {
                db.execSQL(MasterConstants.SQL_CREATE_psituasi);
                db.execSQL(MasterConstants.SQL_CREATE_pskenario);
                db.execSQL(MasterConstants.SQL_CREATE_pskenariopoints);
                db.execSQL(MasterConstants.SQL_CREATE_smanuver);
                db.execSQL(MasterConstants.SQL_CREATE_smanuverpoints);
                db.execSQL(MasterConstants.SQL_CREATE_smarka);
                db.execSQL(MasterConstants.SQL_CREATE_smarkapoints);
                db.execSQL(MasterConstants.SQL_CREATE_sobyek);
                db.execSQL(MasterConstants.SQL_CREATE_cuaca);
                db.execSQL(MasterConstants.SQL_CREATE_chat);
                db.execSQL(MasterConstants.SQL_CREATE_roomchat);
            }
            if (oldVersion < 33) {
                db.execSQL(MasterConstants.SQL_CREATE_kendali);
                db.execSQL(MasterConstants.SQL_CREATE_kendalipoints);
            }

            if (oldVersion < 35) {
                db.execSQL(MasterConstants.SQL_CREATE_identity);
            }
            if (oldVersion < 37) {
//				db.execSQL(MasterConstants.SQL_DROP_roomchat);
//				db.execSQL(MasterConstants.SQL_CREATE_roomchat);
            }
            if (oldVersion < 41) {
                db.execSQL(MasterConstants.SQL_CREATE_manual_ops);
            }
//			if (oldVersion < 42) {
//				db.execSQL(MasterConstants.SQL_DROP_friends);
//				db.execSQL(MasterConstants.SQL_CREATE_friends);
//			}
//			if (oldVersion < 47) {
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_2);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_3);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_4);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_5);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_6);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_7);
//				db.execSQL(MasterConstants.SQL_INSERT_FRIENDS_8);
//				db.execSQL(MasterConstants.SQL_ADD_category_tank);
//				db.execSQL(MasterConstants.SQL_ADD_category_troop);
//				db.execSQL(MasterConstants.SQL_ADD_category_anoa);
//			}
//			if (oldVersion < 48) {
//				db.execSQL(MasterConstants.INS_DEFAULT_IDENTITY);
//				db.execSQL(MasterConstants.INS_DEFAULT_MANUAL_OPS);
//			}
            if (oldVersion < 50) {
//				db.execSQL(MasterConstants.SQL_CREATE_friend_category);
//				db.execSQL(MasterConstants.SQL_ADD_category_tank);
//				db.execSQL(MasterConstants.SQL_ADD_category_troop);
            }
            if (oldVersion < 51) {
//				db.execSQL(MasterConstants.SQL_CREATE_subtype);
                db.execSQL(MasterConstants.SQL_DROP_friends);
                db.execSQL(MasterConstants.SQL_CREATE_friends_2);
            }
            if (oldVersion < 52) {
                db.execSQL(MasterConstants.SQL_DROP_friends);
                db.execSQL(MasterConstants.SQL_CREATE_friends_3);
            }
            if (oldVersion < 53) {
                db.execSQL(MasterConstants.SQL_DROP_foes);
                db.execSQL(MasterConstants.SQL_CREATE_foes_2);
            }
            if (oldVersion < 54) {
                db.execSQL(MasterConstants.SQL_DROP_friend_category);
                db.execSQL(MasterConstants.SQL_DROP_friends);
                db.execSQL(MasterConstants.SQL_CREATE_friends_4);
            }
            if (oldVersion < 55) {
                db.execSQL(MasterConstants.SQL_DROP_friends);
                db.execSQL(MasterConstants.SQL_CREATE_friends_5);
            }
            if (oldVersion < 56) {
                db.execSQL(MasterConstants.SQL_DROP_friend_type);
                db.execSQL(MasterConstants.SQL_DROP_friend_subtype);
                db.execSQL(MasterConstants.SQL_CREATE_type);
                db.execSQL(MasterConstants.SQL_CREATE_subtype);
            }
            if (oldVersion < 57) {	//R27
                db.execSQL(MasterConstants.SQL_ALTER_identity);
            }
            if (oldVersion < 58) {
                db.execSQL(MasterConstants.SQL_ALTER_identity2);
            }
        }

    }

    public Cursor getPoiCategory(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};
            return mDatabase.rawQuery(STAT_getPoiCategory, args);
        }

        return null;
    }

    public Cursor getFriendCategory(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};
            return mDatabase.rawQuery(STAT_getFriendCategory, args);
        }

        return null;
    }

    public void LoadActivityListFromResource(final SQLiteDatabase db) {
        /*db.execSQL(MasterConstants.SQL_CREATE_drop_activity);
        db.execSQL(MasterConstants.SQL_CREATE_activity);
        String[] act = mCtx.getResources().getStringArray(R.array.track_activity);
        for(int i = 0; i < act.length; i++){
            db.execSQL(String.format(MasterConstants.SQL_CREATE_insert_activity, i, act[i]));
        } */
    }

    public void addPoiCategory(final String title, final int hidden, final int iconid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, title);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            this.mDatabase.insert(CATEGORY, null, cv);
        }
    }

    public void updatePoiCategory(final int id, final String title, final int hidden, final int iconid, final int minzoom) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, title);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            cv.put(MINZOOM, minzoom);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(CATEGORY, cv, UPDATE_CATEGORY, args);
        }
    }

    public void addFriendCategory(final String title, final int hidden, final int iconid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, title);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            this.mDatabase.insert(FRIEND_CATEGORY, null, cv);
        }
    }

    public void updateFriendCategory(final int id, final String title, final int hidden, final int iconid, final int minzoom) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, title);
            cv.put(HIDDEN, hidden);
            cv.put(ICONID, iconid);
            cv.put(MINZOOM, minzoom);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(FRIEND_CATEGORY, cv, UPDATE_CATEGORY, args);
        }
    }

    public void DeleteAllPoi() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllPoi);
        }
    }

    public void deleteAllSkenario() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllSkenario);
            mDatabase.execSQL(STAT_DeleteAllSkenarioPoints);
        }
    }

    public void DeleteAllFoe() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllFoe);
        }
    }

    public void deleteAllType() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllType);
        }
    }

    public void deleteAllSubType() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllSubType);
        }
    }

    public void truncateFoes() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_TruncateFoes);
        }
    }

    public void addFoe(int foeid, double lat, double lon, String title, String descr, int symbolid, int flag, long date) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("foeid", foeid);
            cv.put("lat", lat);
            cv.put("lon", lon);
            cv.put("title", title);
            cv.put("descr", descr);
            cv.put("symbolid", symbolid);
            cv.put("flag", flag);
            cv.put("datecreated", date);
            this.mDatabase.insert(FOES, null, cv);
        }
    }

    public void beginTransaction(){
        mDatabase.beginTransaction();
    }

    public void rollbackTransaction(){
        mDatabase.endTransaction();
    }

    public void commitTransaction(){
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public Cursor getTrackListCursor(final String units) {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format(STAT_getTrackList, units), null);
        }

        return null;
    }

    public Cursor getSkenarioListCursor(final String units) {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format(STAT_getSkenarioList, units), null);
        }

        return null;
    }

    Cursor getKendaliListCursor(final String units) {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format(STAT_getKendaliList, units), null);
        }

        return null;
    }

    public long addTrack(final String name, final String descr, final int show, final int cnt, final double distance,
                         final double duration, final int category, final int activity, final Date date, final String style) {
        long newId = -1;

        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            newId = this.mDatabase.insert(TRACKS, null, cv);
        }

        return newId;
    }

    //public long addSkenario(final String name, final String descr, final int show, final int cnt, final double distance,
    //	final double duration, final int category, final int activity, final Date date, final String style) {
    public long addSkenario(final String name, final String descr, final int show, final int cnt, final double distance,
                            final double duration, final int category, final int activity, final Date date, final String style, final int skenariosrcid) {
        long newId = -1;

        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            cv.put("skenariosrcid", skenariosrcid);
            newId = this.mDatabase.insert(SKENARIO, null, cv);
        }

        return newId;
    }

    //public long addSkenarioWithId(final int id, final String name, final String descr, final int show, final int cnt, final double distance,
    //	final double duration, final int category, final int activity, final Date date, final String style) {
    public long addSkenarioWithId(final int id, final String name, final String descr, final int show, final int cnt, final double distance,
                                  final double duration, final int category, final int activity, final Date date, final String style, final int skenariosrcid) {

        long newId = -1;

        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pskenarioid", id);
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            cv.put("skenariosrcid", skenariosrcid);
            newId = this.mDatabase.insert(SKENARIO, null, cv);
        }

        return newId;
    }

    public long addSkenarioPoint(final long skenarioId, final ArrayList<GeoPoint> gPoints) {
        long newId;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            for(int i=0;i<gPoints.size();i++) {
                cv.put("pskenarioid", skenarioId);
                cv.put("lat", gPoints.get(i).getLatitude());
                cv.put("lon", gPoints.get(i).getLongitude());
                cv.put("alt", "0");
                cv.put("speed", "0");
                cv.put("date", "0");
                newId = this.mDatabase.insert(SKENARIOPOINTS, null, cv);
            }
        }

        return 0;
    }

    //public void updateSkenario(final int id, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style) {
    public void updateSkenario(final int id, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style, final int skenariosrcid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            cv.put("skenariosrcid", skenariosrcid);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(SKENARIO, cv, UPDATE_SKENARIO, args);
        }
    }

    //public void updateSkenarioId(final int id, final int newId, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style) {
    public void updateSkenarioId(final int id, final int newId, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style, final int skenariosrcid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pskenarioid", id);
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            cv.put("skenariosrcid", skenariosrcid);
            final String[] args = {Integer.toString(newId)};
            this.mDatabase.update(SKENARIO, cv, UPDATE_SKENARIO, args);
        }
    }

    public void addSkenarioPoint(final long trackid, final double lat,
                                 final double lon, final double alt, final double speed,
                                 final Date date) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(TRACKID, trackid);
            cv.put(LAT, lat);
            cv.put(LON, lon);
            cv.put(ALT, alt);
            cv.put(SPEED, speed);
            cv.put(DATE, date.getTime()/1000);
            this.mDatabase.insert(SKENARIOPOINTS, null, cv);
        }
    }

    public void addSkenarioPointWithId(final int id, final long trackid, final double lat,
                                       final double lon, final double alt, final double speed,
                                       final Date date) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pskenarioid", id);
            cv.put(TRACKID, trackid);
            cv.put(LAT, lat);
            cv.put(LON, lon);
            cv.put(ALT, alt);
            cv.put(SPEED, speed);
            cv.put(DATE, date.getTime()/1000);
            this.mDatabase.insert(SKENARIOPOINTS, null, cv);
        }
    }

    public long addKendali(final String name, final String descr, final int show, final int cnt, final double distance,
                           final double duration, final int category, final int activity, final Date date, final String style) {
        long newId = -1;

        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            newId = this.mDatabase.insert(TRACKS, null, cv);
        }

        return newId;
    }

    public void updateTrack(final int id, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(TRACKS, cv, UPDATE_TRACKS, args);
        }
    }

    public void updateKendali(final int id, final String name, final String descr, final int show, final int cnt, final double distance, final double duration, final int category, final int activity, final Date date, final String style) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(DESCR, descr);
            cv.put(SHOW, show);
            cv.put(CNT, cnt);
            cv.put(DISTANCE, distance);
            cv.put(DURATION, duration);
            cv.put(CATEGORYID, category);
            cv.put(ACTIVITY, activity);
            cv.put(DATE, date.getTime()/1000);
            cv.put(STYLE, style);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(TRACKS, cv, UPDATE_TRACKS, args);
        }
    }

    public void addTrackPoint(final long trackid, final double lat,
                              final double lon, final double alt, final double speed,
                              final Date date) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(TRACKID, trackid);
            cv.put(LAT, lat);
            cv.put(LON, lon);
            cv.put(ALT, alt);
            cv.put(SPEED, speed);
            cv.put(DATE, date.getTime()/1000);
            this.mDatabase.insert(TRACKPOINTS, null, cv);
        }
    }

    public void addKendaliPoint(final long kendaliid, final double lat,
                                final double lon, final double alt, final double speed,
                                final Date date) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(TRACKID, kendaliid);
            cv.put(LAT, lat);
            cv.put(LON, lon);
            cv.put(ALT, alt);
            cv.put(SPEED, speed);
            cv.put(DATE, date.getTime()/1000);
            this.mDatabase.insert(TRACKPOINTS, null, cv);
        }
    }

    public Cursor getTrackChecked() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getTrackChecked, null);
        }

        return null;
    }

    public Cursor getKendaliChecked() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getKendaliChecked, null);
        }

        return null;
    }

    public Cursor getSkenarioChecked() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getSkenarioChecked, null);
        }

        return null;
    }

    public Cursor getTrack(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getTrack, args);
        }

        return null;
    }

    public Cursor getSkenario(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getSkenario, args);
        }

        return null;
    }

    public Cursor getKendali(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getKendali, args);
        }

        return null;
    }

    public Cursor getTrackPoints(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getTrackPoints, args);
        }

        return null;
    }

    public Cursor getKendaliPoints(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getKendaliPoints, args);
        }

        return null;
    }

    public Cursor getSkenarioPoints(final long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            return mDatabase.rawQuery(STAT_getSkenarioPoints, args);
        }

        return null;
    }


    public Cursor getTP() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getTP, null);
        }

        return null;
    }

    public Cursor getKP() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getKP, null);
        }

        return null;
    }

    public void setTrackChecked(final int id){
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            mDatabase.execSQL(STAT_setTrackChecked_1, args);
        }
    }

    public void setKendaliChecked(final int id){
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            mDatabase.execSQL(STAT_setKendaliChecked_1, args);
        }
    }

    public void deleteTrack(final int id) {
        if (isDatabaseReady()) {
            beginTransaction();
            final String[] args = {Long.toString(id)};
            mDatabase.execSQL(STAT_deleteTrack_1, args);
            mDatabase.execSQL(STAT_deleteTrack_2, args);
            commitTransaction();
        }
    }

    public void deleteSkenario(final int id) {
        if (isDatabaseReady()) {
            beginTransaction();
            final String[] args = {Long.toString(id)};
            mDatabase.execSQL(STAT_deleteSkenario_1, args);
            mDatabase.execSQL(STAT_deleteSkenario_2, args);
            commitTransaction();
        }
    }

    public void deleteKendali(final int id) {
        if (isDatabaseReady()) {
            beginTransaction();
            final String[] args = {Long.toString(id)};
            mDatabase.execSQL(STAT_deleteTrack_1, args);
            mDatabase.execSQL(STAT_deleteTrack_2, args);
            commitTransaction();
        }
    }

    public int saveTrackFromWriter(final SQLiteDatabase db){
        int res = 0;
        if (isDatabaseReady()) {
            final Cursor c = db.rawQuery(STAT_saveTrackFromWriter, null);
            if(c != null){
                if(c.getCount() > 1){
                    beginTransaction();

                    res = c.getCount();
                    long newId = -1;

                    final ContentValues cv = new ContentValues();
                    cv.put(NAME, TRACK);
                    cv.put(SHOW, 0);
                    cv.put(ACTIVITY, 0);
                    cv.put(CATEGORYID, 0);
                    newId = mDatabase.insert(TRACKS, null, cv);
                    res = (int) newId;

                    cv.put(NAME, TRACK+ONE_SPACE+newId);
                    if (c.moveToFirst()) {
                        cv.put(DATE, c.getInt(4));
                    }
                    final String[] args = {Long.toString(newId)};
                    mDatabase.update(TRACKS, cv, UPDATE_TRACKS, args);

                    if (c.moveToFirst()) {
                        do {
                            cv.clear();
                            cv.put(TRACKID, newId);
                            cv.put(LAT, c.getDouble(0));
                            cv.put(LON, c.getDouble(1));
                            cv.put(ALT, c.getDouble(2));
                            cv.put(SPEED, c.getDouble(3));
                            cv.put(DATE, c.getInt(4));
                            mDatabase.insert(TRACKPOINTS, null, cv);
                        } while (c.moveToNext());
                    }

                    commitTransaction();
                }
                c.close();

                db.execSQL(STAT_CLEAR_TRACKPOINTS);
            }

        }

        return res;
    }

    public Cursor getMixedMaps() {
        if (isDatabaseReady())
            return mDatabase.rawQuery(STAT_get_maps, null);
        return null;
    }

    public long addMap(int type, String params) {
        long newId = -1;

        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, "New map");
            cv.put(TYPE, type);
            cv.put(PARAMS, params);
            newId = this.mDatabase.insert(MAPS, null, cv);
        }

        return newId;
    }

    public Cursor getMap(long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            // �� ������ ������� �����
            return mDatabase.rawQuery(STAT_get_map, args);
        };
        return null;
    }

    public void updateMap(long id, String name, int type, String params) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            cv.put(TYPE, type);
            cv.put(PARAMS, params);
            final String[] args = {Long.toString(id)};
            this.mDatabase.update(MAPS, cv, UPDATE_MAPS, args);
        }
    }

    public void deleteMap(long id) {
        if (isDatabaseReady()) {
            final String[] args = {Long.toString(id)};
            mDatabase.delete(MAPS, UPDATE_MAPS, args);
        }
    }

    public void addTacticalSymbol(final int mCatId, final String mDescr, final byte[] data) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
//			cv.put("symid", mId);
            cv.put("categoryid", mCatId);
            cv.put("descr", mDescr);
            cv.put("image", data);
            this.mDatabase.insert("tactical_symbol", null, cv);
        }
    }

    public void delTacticalSymbol(int mId) {
        this.mDatabase.delete("tactical_symbol", null, null);
    }

    public void updateTacticalSymbol(final int mId, final int mCatId) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("categoryid", mCatId);
            this.mDatabase.update("tactical_symbol", cv, "symid = "+mId, null);
        }
    }
    //AJ Update Name MyIdentity
    public void updateMyIdentityLabel(final int mId, final String Label) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("title", Label);
            this.mDatabase.update("identity", cv, "id = "+mId, null);
        }
    }

    //AJ Update Name friendIdentity
    public void updateFriendIdentityLabel(final int mAddress, final String Label) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("title", Label);
            this.mDatabase.update("friends", cv, "address = "+mAddress, null);
        }
    }


    public Cursor getSymbolList() {
        if (isDatabaseReady()) {
            String[] column = {SYMID, CATEGORYID, DESCR, IMAGE};
            return mDatabase.rawQuery(STAT_getTacticalSymbol, null);
        }

        return null;
    }

    public Cursor getSmsAck(String cat) {
        if (isDatabaseReady()) {
            //String[] column = {SENDER2,RECEIVER2,ROOMCHATID2,RDATE2, DESCR, MSG2};
            final String[] args = {cat};
            String ACK = "SELECT id _id, isi_sms,id_pengirim,tangal FROM sms_ack WHERE tangal == '"+cat+"' ORDER BY id DESC limit 5";
            return mDatabase.rawQuery(ACK, null);
        }

        return null;
    }
    public Cursor getAllSms(final String cat) {
        if (isDatabaseReady()) {
            //String[] column = {SENDER2,RECEIVER2,ROOMCHATID2,RDATE2, DESCR, MSG2};
            final String[] args = {cat};
            return mDatabase.rawQuery(STAT_getAllSMS, null);
        }

        return null;
    }

    public Cursor getSymbolListCategory(final int cat) {
        if (isDatabaseReady()) {
            String[] column = {SYMID, CATEGORYID, DESCR, IMAGE};
            final String[] args = {Integer.toString(cat)};
            return mDatabase.rawQuery(STAT_getTacticalSymbolCategory, args);

        }

        return null;
    }

    public Cursor getFriendSymbolListCategory(final int cat) {
        if (isDatabaseReady()) {
            String[] column = {SYMID, CATEGORYID, DESCR, IMAGE};
            final String[] args = {Integer.toString(cat)};
            return mDatabase.rawQuery(STAT_getFriendTacticalSymbolCategory, args);
        }

        return null;
    }

    public Cursor getTacticalSymbolImage(final int symid) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(symid)};
            return mDatabase.rawQuery(STAT_getTacticalSymbolSymid, args);
        }

        return null;
    }

    public void updateIdentity(final int id, final int role, final String mmiIp, final String niIp, final long serverPort, final String title, final String descr, final int subtypeid) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("role", role);
            cv.put("mmi_ip", mmiIp);
            cv.put("ni_ip", niIp);
            cv.put("server_port", Long.toString(serverPort));
            cv.put("title", title);
            cv.put("descr", descr);
            cv.put("subtypeid", subtypeid);
            this.mDatabase.delete("identity", null, null);
            this.mDatabase.insert("identity", null, cv);
        }
    }

    public Cursor getManualOpsCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_manual_ops, null);
        }

        return null;
    }

    public void updateManualOps(final int id, final int ismanloc, final float lat, final float lon, final int ismanhtank, final int htank, final int ismanhkanon, final int hkanon, final int ismanfuel, final float fuel, final int ismanmunisikanon, final int munisikanon, final int ismanmunisigun, final int munisigun, final int ismansuhu, final float suhu, final int ismankelembaban, final float kelembaban) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("ismanloc", ismanloc);
            cv.put("lat", lat);
            cv.put("lon", lon);
            cv.put("ismanhtank", ismanhtank);
            cv.put("ismanhkanon", ismanhkanon);
            cv.put("ismanfuel", ismanfuel);
            cv.put("ismanmunisikanon", ismanmunisikanon);
            cv.put("ismanmunisigun", ismanmunisigun);
            cv.put("ismansuhu", ismansuhu);
            cv.put("ismankelembaban", ismankelembaban);
            cv.put("htank", htank);
            cv.put("hkanon", hkanon);
            cv.put("munisikanon", munisikanon);
            cv.put("munisigun", munisigun);
            cv.put("fuel", fuel);
            cv.put("suhu", suhu);
            cv.put("kelembaban", kelembaban);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update("manual_ops", cv, "id = @1", args);
        }
    }

// --- FRIEND ---
//	public void addFriend(final double aLat, final double aLon, final String aType, final String aLabel, final String aSymbol,
//			final String aAddress, final String aIp, final int aIsactive) {
//		if (isDatabaseReady()) {
//			final ContentValues cv = new ContentValues();
//			cv.put(LAT, aLat);
//			cv.put(LON, aLon);
//			cv.put(TYPE, aType);
//			cv.put(LABEL, aLabel);
//			cv.put(SYMBOL, aSymbol);
//			cv.put(ADDRESS, aAddress);
//			cv.put(IP, aIp);
//			cv.put(ISACTIVE, aIsactive);
//			this.mDatabase.insert(FRIENDS, null, cv);
//		}
//	}

    public void updateFriend(final int id, final double aLat, final double aLon, final String aType, final String aLabel, final String aSymbol,
                             final String aAddress, final String aIp, final int aIsactive) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(LAT, aLat);
            cv.put(LON, aLon);
            cv.put(TYPE, aType);
            cv.put(LABEL, aLabel);
            cv.put(SYMBOL, aSymbol);
            cv.put(ADDRESS, aAddress);
            cv.put(IP, aIp);
            cv.put(ISACTIVE, aIsactive);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(FRIENDS, cv, UPDATE_FRIEND, args);
        }
    }

    public void updateFriendLoc(final int address, final double lat, final double lon, final float heading1, final float heading2) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(LAT, lat);
            cv.put(LON, lon);
            cv.put(HEADING1, heading1);
            cv.put(HEADING2, heading2);
            final String[] args = {Integer.toString(address)};
            this.mDatabase.update(FRIENDS, cv, FRIEND_ADDRESS, args);
        }
    }

    public void updateFriendPar(final int address, final double bbLevel, final int ammoRest) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(FUEL, bbLevel);
            cv.put(CANON, ammoRest);
            final String[] args = {Integer.toString(address)};
            this.mDatabase.update(FRIENDS, cv, FRIEND_ADDRESS, args);
        }
    }


    public Cursor getFriendListCursor() {
        if(isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_FRIEND_LIST, null);
        }

        return null;
    }

    public Cursor getFriendListCursor2() {
        if(isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_GET_FRIEND_LIST2, null);
        }

        return null;
    }

    public Cursor getFriendListNotHiddenCursor(final int zoom, final double left, final double right, final double top, final double bottom) {
        if (isDatabaseReady()) {
            final String[] args = {Double.toString(left),Double.toString(right),Double.toString(bottom),Double.toString(top)};

            return mDatabase.rawQuery(STAT_FriendListNotHidden, args);
        }

        return null;
    }

    public Cursor getFriendListActiveCursor(final int zoom, final double left, final double right, final double top, final double bottom) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(zoom + 1),Double.toString(left),Double.toString(right),Double.toString(bottom),Double.toString(top)};

            return mDatabase.rawQuery(STAT_FriendListActive, args);
        }

        return null;
    }

    public String getFriendListActiveBoolean() {

        return "ok";
    }

    public Cursor getFriendByAddres(final int address) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(address)};

            return mDatabase.rawQuery(STAT_getFriendByAddress, args);
        }

        return null;
    }

    public Cursor getFriend(final int id) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(id)};

            return mDatabase.rawQuery(STAT_getFriend, args);
        }

        return null;
    }

    public void deleteFriend(final int id) {
        if (isDatabaseReady()) {
            final Double[] args = {Double.valueOf(id)};
            mDatabase.execSQL(STAT_deleteFriend, args);
        }
    }

    public void DeleteAllFriend() {
        if (isDatabaseReady()) {
            mDatabase.execSQL(STAT_DeleteAllFriend);
        }
    }

    public Cursor getFriendCategoryListCursor() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_FriendCategoryList, null);
        }

        return null;
    }

    public void updateFriend(int id, int address, int parentid, int typeid, int subtypeid,
                             String title, String descr) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("address", address);
            cv.put("parentid", parentid);
            cv.put("typeid", typeid);
            cv.put("subtypeid", subtypeid);
            cv.put("title", title);
            cv.put("descr", descr);
            final String[] args = {Integer.toString(id)};
            this.mDatabase.update(FRIENDS, cv, UPDATE_FRIEND, args);
        }
    }

    public long addFriend(int address, int parentid, int typeid, int subtypeid, String title, String descr) {
        long newId = -1;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("address", address);
            cv.put("parentid", parentid);
            cv.put("typeid", typeid);
            cv.put("subtypeid", subtypeid);
            cv.put("title", title);
            cv.put("descr", descr);
            newId = this.mDatabase.insert(FRIENDS, null, cv);
        }
        return newId;
    }

    public boolean isManLoc() {
        if (isDatabaseReady()) {
            Cursor c = mDatabase.rawQuery(STAT_GET_manual_ops, null);
            int ismanloc = 0;
            if(c != null) {
                if(c.moveToFirst()) {
                    do {
                        ismanloc = c.getInt(1);
                    } while(c.moveToNext());
                }
            }
            if(ismanloc == 1)
                return true;
            else
                return false;
        }
        return false;
    }

    public String getLoc() {
        if (isDatabaseReady()) {
            Cursor c = mDatabase.rawQuery(STAT_GET_manual_ops, null);
            double lat = 0, lon = 0;
            if(c != null) {
                if(c.moveToFirst()) {
                    do {
                        lat = c.getDouble(2);
                        lon = c.getDouble(3);
                    } while(c.moveToNext());
                }
            }
            return lat + "," + lon;
        }
        return null;
    }

    public void setFriendStatus(final int address, final int status) {
        if (isDatabaseReady()) {
            final String[] args = {Integer.toString(address), Integer.toString(status)};
            mDatabase.rawQuery(STAT_updateFriendStatus, args);
            Log.d(TAG, "args : " + args[0].toString());
            Log.d(TAG, "args : " + args[1].toString());
        }
    }

    public void DeleteDataBase(){
        if(isDatabaseReady()){
            mDatabase.execSQL("DROP DATABASE IF EXIST geodata");
        }
    }


    //FTP
    public boolean isEmptyFtp() {
        boolean isEmpty = true;
        if (isDatabaseReady()) {
            Cursor c = mDatabase.rawQuery(SQL_COUNT_FTP, null);
            if(c.getCount() > 0)
                isEmpty = false;
            else
                isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmptyPoi() {
        boolean isEmpty = true;
        if (isDatabaseReady()) {
            Cursor c = mDatabase.rawQuery(SQL_COUNT_POI, null);
            if(c.getCount() > 0)
                isEmpty = false;
            else
                isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmptySkn() {
        boolean isEmpty = true;
        if (isDatabaseReady()) {
            Cursor c = mDatabase.rawQuery(SQL_COUNT_SKN, null);
            if(c.getCount() > 0)
                isEmpty = false;
            else
                isEmpty = true;
        }
        return isEmpty;
    }

	/*public int PointSrcId(int address){
		int A = address;
		int B = 0;
		if (isDatabaseReady()) {
			Cursor c = mDatabase.rawQuery(SQL_PointSrcID,null);
			for(int i = 0; i < c.getCount();i++){
				/*if(((c.getInt(i) >> 16) & 0xffff) == A){
					B = c.getInt(i) & 0xffff;
				}else{
					B = 1;
				}
				B = c.getInt(i);
			}
		}
		return B;
	}*/

    public Cursor FtpFileId(){
        if(isDatabaseReady()){
            return mDatabase.rawQuery( SQL_FTPFileID, null);
        }
        return null;
    }

    public Cursor PointSrcId(){
        if(isDatabaseReady()){
            return mDatabase.rawQuery( SQL_PointSrcID, null);
        }
        return null;
    }

    public Cursor getStatusPoi() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(SQL_COUNT_POI, null);
        }

        return null;
    }

    public Cursor PointId(){
        if(isDatabaseReady()){
            return mDatabase.rawQuery( SQL_PointID, null);
        }
        return null;
    }

    public Cursor SkenaId(){
        if(isDatabaseReady()){
            return mDatabase.rawQuery( SQL_SkenaID, null);
        }
        return null;
    }

    public Cursor SkenarioId(){
        if(isDatabaseReady()){

            return mDatabase.rawQuery(SQL_SearchSkenaSrcId, null);
        }
        return null;
    }

    public Cursor getSubTypeID() {
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(STAT_getSubtype, null);
        }

        return null;
    }

    public void updateFriendSubtype(final int address, final int subtype) {
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put(SUBTYPEID, subtype);
            final String[] args = {Integer.toString(address)};
            this.mDatabase.update(FRIENDS, cv, FRIEND_ADDRESS, args);
        }
    }

    public void updateIdentity(final int address, final int subtype) {
        if (isDatabaseReady()) {
            Log.i(TAG, "add" + address + "" + subtype);
            final ContentValues cv = new ContentValues();
            cv.put(SUBTYPEID, subtype);
            final String[] args = {Integer.toString(address)};
            this.mDatabase.update(IDENTITY, cv, ID, args);
        }
    }


	/*public int SearchSkenaSrcId(final int id) {
		if (isDatabaseReady()) {
			final String[] args = {Long.toString(id)};
			id = mDatabase.rawQuery(SQL_SearchSkenaSrcId, args);

		}
		return id;
	}*/
}
