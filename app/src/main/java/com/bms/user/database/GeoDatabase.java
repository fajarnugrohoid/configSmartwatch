package com.bms.user.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bms.user.model.ModelSetIp;
import com.bms.user.model.ModelSettingSmartwatch;
import com.bms.user.utils.MasterConstants;
import com.bms.user.utils.RSQLiteOpenHelper;
import com.bms.user.utils.Ut;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public Cursor getTrackPointCursor() {
        if(isDatabaseReady()) {
            return mDatabase.rawQuery("SELECT * FROM trackpoints", null);
        }

        return null;
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
                Log.d(TAG, "GeoDatabase ret is false");
                //Toast.makeText(mCtx, mCtx.getText("GeoDatabase Not Avala"), Toast.LENGTH_LONG).show();
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

    public Cursor getListSkenario(){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT p.pskenarioid,p.name,p.descr,p.categoryid, p.date, p.cnt,  p.skenariosrcid ,ps.id,ps.lat, ps.lon, p.descr, p.categoryid, p.style, p.skenariosrcid FROM pskenario as p\n" +
                    "INNER JOIN pskenariopoints as ps ON (p.pskenarioid=ps.pskenarioid)", ""), null);
        }
        return null;
    }

    public Cursor getListTacticalSymbol(){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT * FROM psituasi", ""), null);
        }
        return null;
    }

    public Cursor getTacticalSymbol(int symbolId){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT * FROM tactical_symbol WHERE symid=" + symbolId, ""), null);
        }
        return null;
    }

    public long insertSkenario(String name, String descr, String date,
                               String show, int cnt, String duration, String distance,
                               int categoryid, String activity, String style, String skenariosrcid){
        long newId = 0;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            //cv.put("pskenarioid", pskenarioid);
            cv.put("name", name);
            cv.put("descr", descr);
            cv.put("date", date);
            cv.put("show", show);
            cv.put("cnt", cnt);
            cv.put("duration", duration);
            cv.put("distance", distance);
            cv.put("categoryid", categoryid);
            cv.put("activity", activity);
            cv.put("style", style);
            cv.put("skenariosrcid", skenariosrcid);
            newId = this.mDatabase.insert("pskenario", null, cv);
        }
        return newId;
    }

    public long insertTacticalSymbol(String name, String descr, Double lat,
                                     Double lon, Double alt, int hidden, int categoryid,
                                     int pointsourceid, int iconid){
        long newId = 0;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            //cv.put("psituasiid", psituasiid);
            cv.put("name", name);
            cv.put("descr", descr);
            cv.put("lat", lat);
            cv.put("lon", lon);
            cv.put("alt", alt);
            cv.put("hidden", hidden);
            cv.put("categoryid", categoryid);
            cv.put("pointsourceid", pointsourceid);
            cv.put("iconid", iconid);
            newId = this.mDatabase.insert("psituasi", null, cv);
        }
        return newId;
    }

    public void insertSkenarioPoints(int pskenarioid, Double lat, Double lon, String alt,
                               String speed, String date){
        long newId;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("pskenarioid", pskenarioid);
            cv.put("lat", lat);
            cv.put("lon", lon);
            cv.put("alt", alt);
            cv.put("speed", speed);
            cv.put("date", date);
            newId = this.mDatabase.insert("pskenariopoints", null, cv);
        }
    }


    public long updateSettingSmartwatch(ModelSettingSmartwatch modelSettingSmartwatch){
        long res = 0;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            if ( (modelSettingSmartwatch.getSendTo()!=null) && (!modelSettingSmartwatch.getSendTo().equalsIgnoreCase("")) ) {
                cv.put("send_to", modelSettingSmartwatch.getSendTo());
            }
            if ( (modelSettingSmartwatch.getMapsName()!=null) && (!modelSettingSmartwatch.getMapsName().equalsIgnoreCase(""))){
                cv.put("maps_name", modelSettingSmartwatch.getMapsName());
            }
            if ( (modelSettingSmartwatch.getMapsPath()!=null) && (!modelSettingSmartwatch.getMapsPath().equalsIgnoreCase(""))){
                cv.put("maps_path", modelSettingSmartwatch.getMapsPath());
            }
            if ( (modelSettingSmartwatch.getGroup()!=null) && (!modelSettingSmartwatch.getGroup().equalsIgnoreCase(""))){
                cv.put("set_group", modelSettingSmartwatch.getGroup());
            }
            if ( (modelSettingSmartwatch.getIpCCU()!=null) && (!modelSettingSmartwatch.getIpCCU().equalsIgnoreCase("")) ) {
                cv.put("ip_ccu", modelSettingSmartwatch.getIpCCU());
            }
            if ( (modelSettingSmartwatch.getIpDriverView()!=null) && (!modelSettingSmartwatch.getIpDriverView().equalsIgnoreCase("")) ) {
                cv.put("ip_driverview", modelSettingSmartwatch.getIpDriverView());
            }

            if ( (modelSettingSmartwatch.getImageFileNameEnding()!=null) && (!modelSettingSmartwatch.getImageFileNameEnding().equalsIgnoreCase(""))) {
                cv.put("image_file_name_ending", modelSettingSmartwatch.getImageFileNameEnding());
            }
            if (modelSettingSmartwatch.getMinZoomLvl()!=0){
                cv.put("min_zoom_lvl", modelSettingSmartwatch.getMinZoomLvl());
            }

            if (modelSettingSmartwatch.getMaxZoomLvl()!=0){
                cv.put("max_zoom_lvl", modelSettingSmartwatch.getMaxZoomLvl());
            }

            if (modelSettingSmartwatch.getFirstZoomLvl()!=0){
                cv.put("first_zoom_lvl", modelSettingSmartwatch.getFirstZoomLvl());
            }

            if (modelSettingSmartwatch.getTileSizePixel()!=0){
                cv.put("tile_size_pixel", modelSettingSmartwatch.getTileSizePixel());
            }

            if ( (modelSettingSmartwatch.getSdrLat()!=null) && (!modelSettingSmartwatch.getSdrLat().equalsIgnoreCase("")) ){
                cv.put("sdr_lat", modelSettingSmartwatch.getSdrLat());
            }

            if ( (modelSettingSmartwatch.getSdrLon()!=null) && (!modelSettingSmartwatch.getSdrLon().equalsIgnoreCase("")) ){
                cv.put("sdr_lon", modelSettingSmartwatch.getSdrLon());
            }

            if (modelSettingSmartwatch.getIsCompassExternal()!=null){
                cv.put("is_compass_external", modelSettingSmartwatch.getIsCompassExternal());
            }

            if (modelSettingSmartwatch.getSmartwatchId()!=null){
                cv.put("id_smartwatch", modelSettingSmartwatch.getSmartwatchId());
            }

            res = this.mDatabase.update("setting_smartwatch",  cv, "_id=1", null);
            Log.d(TAG, "res:" + res);
        }
        return res;
    }

    public long updateSetIp(String SSIDName, String SSIDPassword, String myIpAddress,
                            String myGateway){
        long res = 0;
        Log.d(TAG, SSIDName + "-" + SSIDPassword + "-" + myIpAddress + "-" + myGateway);
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            if (!SSIDName.equalsIgnoreCase("")){
                cv.put("ssid_name", SSIDName);
            }
            if (!SSIDPassword.equalsIgnoreCase("")){
                cv.put("ssid_password", SSIDPassword);
            }
            if (!myIpAddress.equalsIgnoreCase("")){
                cv.put("my_ip_address", myIpAddress);
            }
            if (!myGateway.equalsIgnoreCase("")){
                cv.put("my_gateway", myGateway);
            }

            res = this.mDatabase.update("setting_wifi",  cv, "_id=1", null);
            Log.d(TAG, "res:" + res);
        }
        return res;
    }

    public Cursor getSettingSmartwatch(){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT * FROM setting_smartwatch", ""), null);
        }
        return null;
    }

    public int deleteSkenarioById(int pSkenarioId) {
        Log.d(TAG, "DbManager deleteSkenarioById");
        int res = 0;
        if (isDatabaseReady()) {

            res = mDatabase.delete("pskenario", "pskenarioid=" + pSkenarioId, null);

            Log.d(TAG, "DbManager deleteSkenarioById c1 " + res);
            if (res >= 1) {
                res = mDatabase.delete("pskenariopoints", "pskenarioid=" + pSkenarioId, null);
                Log.d(TAG, "DbManager deleteSkenarioById c2 " + res);
            }

        }
        return res;
    }

    public Cursor getWifiConfig(){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT * FROM setting_wifi", ""), null);
        }
        return null;
    }

    public int clearAll() {
        int resSkenario = 0,resSkenarioPoints=0, resSituasi=0, resChat=0, resPoints=0;
        if (isDatabaseReady()) {

            resSkenario = mDatabase.delete("pskenario", null, null);
            resSkenarioPoints = mDatabase.delete("pskenariopoints", null, null);
            resSituasi = mDatabase.delete("psituasi", null, null);
            resChat = mDatabase.delete("chat", null, null);
            resPoints = mDatabase.delete("points", null, null);

            Log.d(TAG, "DbManager clear all" + resSkenario);
        }
        return resSkenario;
    }

    public long insertSMS(String sms){
        long smsId = 0;
        if (isDatabaseReady()) {
            final ContentValues cv = new ContentValues();
            cv.put("roomchatid", 1);
            cv.put("sender", 39);
            cv.put("receiver", 40);
            cv.put("message", sms);
            smsId = this.mDatabase.insert("chat", null, cv);
        }
        return smsId;
    }

    public Cursor getListSMS(){
        if (isDatabaseReady()) {
            return mDatabase.rawQuery(String.format("SELECT * FROM chat", ""), null);
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
            //mDatabase.execSQL("DROP DATABASE IF EXIST geodata");
        }
    }

}
