package com.bms.user.bmssmartwatch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import org.andnav.osm.util.GeoPoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class DbManager implements MasterConstants {
    protected final Context mCtx;
    private GeoDatabase mGeoDatabase;
    private boolean mStopProcessing;
    private final String TAG = "DbManager";

    public DbManager(Context ctx) {
        super();
        mCtx = ctx;
        mGeoDatabase = new GeoDatabase(ctx);
    }

    public GeoDatabase getGeoDatabase(){
        return mGeoDatabase;
    }

    public void FreeDatabases(){
        mGeoDatabase.FreeDatabases();
    }

    public void StopProcessing() {
        mStopProcessing = true;
    }

    private boolean Stop() {
        if(mStopProcessing) {
            mStopProcessing = false;
            return true;
        }
        return false;
    }

    public void addPoi(final String title, final String descr, Integer PointSrcID, GeoPoint point){
        //mGeoDatabase.addPoi(title, descr, point.getLatitude(), point.getLongitude(), ZERO, ZERO, ZERO, ZERO, R.drawable.poi);
        //PointSrcID = PoiActivity.po.PointSourceIdPoi;
        mGeoDatabase.addPoi(title, descr, point.getLatitude(), point.getLongitude(), ZERO, ZERO, PointSrcID, ZERO, R.drawable.ic_action_location);
    }

    public long addPoiWithId(final PoiPoint point) {
        long newId = -1;
        int B = 0;
        if(point.getPointSrcId() >= 0){
            Log.i(TAG, "AAA");

            Cursor cur = mGeoDatabase.PointId();
            startManagingCursor(cur);

            if(cur != null){
                //for(int i = 0; i < cur.getCount(); i++){
                for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                    if(cur.moveToFirst()){
                        do{
                            int i = 0;
                            B = cur.getInt(i);
                            i++;
                        }while(cur.moveToNext());
                    }
                }

            }

            B = B + 1;
            mGeoDatabase.addPoiWithId(B, point.Title, point.Descr, point.GeoPoint.getLatitude(), point.GeoPoint.getLongitude(), point.Alt, point.CategoryId, point.PointSourceId, point.Hidden == true ? ONE : ZERO, point.IconId);
            newId = B;
        }
        else {
            Log.i(TAG, "BBB");
            newId = 0;
        }

        return newId;
    }

    private void startManagingCursor(Cursor cur) {
        // TODO Auto-generated method stub

    }

    public long updatePoi(final PoiPoint point){
        long newId = -1;
        if(point.getId() < 0)
            newId = mGeoDatabase.addPoi(point.Title, point.Descr, point.GeoPoint.getLatitude(), point.GeoPoint.getLongitude(), point.Alt, point.CategoryId, point.PointSourceId, point.Hidden == true ? ONE : ZERO, point.IconId);
        else
            mGeoDatabase.updatePoi(point.getId(), point.Title, point.Descr, point.GeoPoint.getLatitude(), point.GeoPoint.getLongitude(), point.Alt, point.CategoryId, point.PointSourceId, point.Hidden == true ? ONE : ZERO, point.IconId);

        return newId;
    }

    public void updatePoiId(final PoiPoint point, final int newId) {
        mGeoDatabase.updatePoiId(point.getId(), newId, point.Title, point.Descr, point.GeoPoint.getLatitude(), point.GeoPoint.getLongitude(), point.Alt, point.CategoryId, point.PointSourceId, point.Hidden == true ? ONE : ZERO, point.IconId);
    }

    public boolean isObjectIdExist(final int objectId) {
        //int countId = mGeoDatabase.countObjectId(objectId);
        int countId = mGeoDatabase.countObjectSrcId(objectId);
        if(countId > 0)
            return true;
        else
            return false;
    }

    public boolean isSkenarioIdExist(final int skenarioId) {
        int countId = mGeoDatabase.countSkenarioId(skenarioId);
        if(countId > 0)
            return true;
        else
            return false;
    }

    public boolean isSkenarioSrcIdExist(final int skenarioId) {
        int countId = mGeoDatabase.countSkenarioSrcId(skenarioId);
        if(countId > 0)
            return true;
        else
            return false;
    }

	/*public int SkenaId(final int SkenaSrcId){
		int id = 0;
			id = mGeoDatabase.SearchSkenaSrcId(SkenaSrcId);

		Log.i(TAG, "IDDD : " + id);
		return id;
	}*/

    public void updateSkenarioId(final Skenario skenario, final int newId) {
        mGeoDatabase.updateSkenarioId(skenario.getId(), newId, skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);
    }

    public long addSkenarioWithId(final Skenario skenario, final ArrayList<GeoPoint> gPoints) {
        long newId = -1;
		/*if(skenario.getId() > 0) {
			newId = mGeoDatabase.addSkenarioWithId(skenario.getId(), skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "");
			mGeoDatabase.addSkenarioPoint(skenario.getId(), gPoints);
		} else
			newId = 0;
		*/

        int B = 0;
        if(skenario.getSkenaId() > 0){
            Log.i(TAG, "AAA");

            Cursor cur = mGeoDatabase.SkenaId();
            startManagingCursor(cur);

            if(cur != null){
                //for(int i = 0; i < cur.getCount(); i++){
                for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                    if(cur.moveToFirst()){
                        do{
                            int i = 0;
                            B = cur.getInt(i);
                            i++;
                        }while(cur.moveToNext());
                    }
                }

            }
            B = B + 1;
            mGeoDatabase.addSkenarioWithId(B, skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);
            //mGeoDatabase.addSkenarioPoint(B, gPoints);
            newId = B;
        }
        else {
            Log.i(TAG, "BBB");


            //Add by Andri untuk local freedraw driver view

            Cursor cur = mGeoDatabase.SkenaId();
            startManagingCursor(cur);

            if(cur != null){
                //for(int i = 0; i < cur.getCount(); i++){
                for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                    if(cur.moveToFirst()){
                        do{
                            int i = 0;
                            B = cur.getInt(i);
                            i++;
                        }while(cur.moveToNext());
                    }
                }

            }
            B = B + 1;
            mGeoDatabase.addSkenarioWithId(B, skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);
            //mGeoDatabase.addSkenarioPoint(B, gPoints);

            //End Add

            newId = 0;
        }

        return newId;
    }

    public long updateSkenario(final Skenario skenario, final ArrayList<GeoPoint> gPoints) {
        long skenarioId = -1;
        if(skenario.getId() < 0) {
//			skenarioId = mGeoDatabase.addSkenario(skenario.Name, skenario.Descr, 1, 0, 0, 0, 0, 0, skenario.Date, "");
            skenarioId = mGeoDatabase.addSkenario(skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);

            //mGeoDatabase.addSkenarioPoint(skenarioId, gPoints);
        } else
            //mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "");
            mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);

        return skenarioId;
    }

    public void updateSkenarioData(final Skenario skenario) {
        //mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "");
        mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, 1, skenario.Cnt, 0, 0, 0, 0, skenario.Date, "", skenario.Skenariosrcid);
    }

    private List<PoiPoint> doCreatePoiListFromCursor(Cursor c){
        final ArrayList<PoiPoint> items = new ArrayList<PoiPoint>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items.add(new PoiPoint(c.getInt(4), c.getString(2), c.getString(3), new GeoPoint(
                            (int) (1E6 * c.getDouble(0)), (int) (1E6 * c.getDouble(1))), c.getInt(7), c.getInt(8)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    private List<GridX> doCreateGridxListFromCursor(Cursor c){
        final ArrayList<GridX> items = new ArrayList<GridX>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items.add(new GridX(c.getInt(0), c.getInt(1), c.getInt(2), c.getDouble(3)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    private List<GridY> doCreateGridyListFromCursor(Cursor c){
        final ArrayList<GridY> items = new ArrayList<GridY>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items.add(new GridY(c.getInt(0), c.getInt(1), c.getInt(2), c.getDouble(3)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    private List<FoePoint> doCreateFoeListFromCursor(Cursor c){
        final ArrayList<FoePoint> items = new ArrayList<FoePoint>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    items.add(new FoePoint(c.getInt(4), c.getString(2), c.getString(3), new GeoPoint(
                            (int) (1E6 * c.getDouble(0)), (int) (1E6 * c.getDouble(1))), c.getInt(7)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    public List<PoiPoint> getPoiList() {
        return doCreatePoiListFromCursor(mGeoDatabase.getPoiListCursor());
    }

    public List<PoiPoint> getPoiListNotHidden(int zoom, GeoPoint center, double deltaX, double deltaY){
        return doCreatePoiListFromCursor(mGeoDatabase.getPoiListNotHiddenCursor(zoom, center.getLongitude() - deltaX, center.getLongitude() + deltaX
                , center.getLatitude() + deltaY, center.getLatitude() - deltaY));
    }

    public List<Friend> getFriendListNotHidden(int zoom, GeoPoint center, double deltaX, double deltaY){
        return doCreateFriendListFromCursor(mGeoDatabase.getFriendListNotHiddenCursor(zoom, center.getLongitude() - deltaX, center.getLongitude() + deltaX
                , center.getLatitude() + deltaY, center.getLatitude() - deltaY));
    }

    public List<Friend> getFriendList() {
        return doCreateFriendListAll(mGeoDatabase.getFriendListCursor2());
    }

    private List<Friend> doCreateFriendListFromCursor(Cursor c){
        final ArrayList<Friend> items = new ArrayList<Friend>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
//					SELECT friend.lat, friend.lon, friend.title, friend.descr, friend.friendid, friend.friendid _id, friend.friendid ID, friend.typeid, friend.subtypeid, cat.image, friend.heading1, friend.heading2, cat.hascannon
//					Friend(int id, String title, String descr, GeoPoint mGeoPoint, int typeid, int subtypeid, byte[] image, int hascannon, int bearing, int bearingc)

                    items.add(new Friend(c.getInt(4), c.getString(2), c.getString(3), new GeoPoint(
                            (int) (1E6 * c.getDouble(0)), (int) (1E6 * c.getDouble(1))), c.getInt(7), c.getInt(8), c.getBlob(9), c.getInt(12), c.getInt(10), c.getInt(11)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    private List<Friend> doCreateFriendListAll(Cursor c){
        final ArrayList<Friend> items = new ArrayList<Friend>();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
//					SELECT friend.lat, friend.lon, friend.title, friend.descr, friend.friendid, friend.friendid _id, friend.friendid ID, friend.typeid, friend.subtypeid, cat.image, friend.heading1, friend.heading2, cat.hascannon
//					Friend(int id, String title, String descr, GeoPoint mGeoPoint, int typeid, int subtypeid, byte[] image, int hascannon, int bearing, int bearingc)

                    items.add(new Friend(c.getInt(5), c.getInt(9)));
                } while (c.moveToNext());
            }
            c.close();
        }

        return items;
    }

    public void setFriendStatus(final int address, final int status) {
        mGeoDatabase.setFriendStatus(address, status);
    }

    public List<GridX> getGridx(GeoPoint center, double deltaX, double deltaY) {
        double left = center.getLongitude() - deltaX;
        double right = center.getLongitude() + deltaX;
        double top = center.getLatitude() + deltaY;
        double bottom = center.getLatitude() - deltaY;

        Log.i(TAG, "left: " + left);
        Log.i(TAG, "right: " + right);
        Log.i(TAG, "top: " + top);
        Log.i(TAG, "bottom: " + bottom);

        return doCreateGridxListFromCursor(mGeoDatabase.getGridxList(center.getLongitude() - deltaX, center.getLongitude() + deltaX
                , center.getLatitude() + deltaY, center.getLatitude() - deltaY));
    }

    public List<GridY> getGridy(GeoPoint center, double deltaX, double deltaY) {
        return doCreateGridyListFromCursor(mGeoDatabase.getGridyList(center.getLongitude() - deltaX, center.getLongitude() + deltaX
                , center.getLatitude() + deltaY, center.getLatitude() - deltaY));
    }

    public List<FoePoint> getFoeListNotHidden(int zoom, GeoPoint center, double deltaX, double deltaY){
        return doCreateFoeListFromCursor(mGeoDatabase.getPoiListNotHiddenCursor(zoom, center.getLongitude() - deltaX, center.getLongitude() + deltaX
                , center.getLatitude() + deltaY, center.getLatitude() - deltaY));
    }

    public void addPoiStartActivity(Context ctx, GeoPoint touchDownPoint) {
//        ctx.startActivity((new Intent(ctx, PoiActivity.class)).putExtra(LAT,
//                touchDownPoint.getLatitude()).putExtra(LON,
//                touchDownPoint.getLongitude()));
    }


    public PoiPoint getPoiPoint(int id) {
        PoiPoint point = null;
        final Cursor c = mGeoDatabase.getPoi(id);
        //final Cursor c = mGeoDatabase.getPoiSrcId(id);
        if (c != null) {
            if (c.moveToFirst())
                point = new PoiPoint(c.getInt(4), c.getString(2), c
                        .getString(3), new GeoPoint(
                        (int) (1E6 * c.getDouble(0)), (int) (1E6 * c
                        .getDouble(1))), c.getInt(9), c.getInt(7), c
                        .getInt(5), c.getInt(8), c.getInt(6));
            c.close();
        }

        return point;
    }

    public PoiPoint getPoiSrcPoint(int SrcId) {
        PoiPoint point = null;
        //final Cursor c = mGeoDatabase.getPoi(id);
        final Cursor c = mGeoDatabase.getPoiSrcId(SrcId);
        if (c != null) {
            if (c.moveToFirst())
                point = new PoiPoint(c.getInt(4), c.getString(2), c
                        .getString(3), new GeoPoint(
                        (int) (1E6 * c.getDouble(0)), (int) (1E6 * c
                        .getDouble(1))), c.getInt(9), c.getInt(7), c
                        .getInt(5), c.getInt(8), c.getInt(6));
            c.close();
        }

        return point;
    }

    public Friend getFriendByAddress(int address) {
        Friend friend = null;
        final Cursor c = mGeoDatabase.getFriendByAddres(address);
        if (c != null) {
            if (c.moveToFirst())
//				Friend(int id, int address, int parentid, String title, String descr, int hidden, int typeid, int subtypeid, byte[] image, GeoPoint mGeoPoint)
//				SELECT a.friendid, a.address, a.parentid, a.title, a.descr, a.hidden, a.typeid, a.subtypeid, b.image, a.lat, a.lon

                friend = new Friend(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getBlob(8), new GeoPoint(
                        (int) (1E6 * c.getDouble(9)), (int) (1E6 * c.getDouble(10))));
            c.close();
        }

        return friend;
    }

    public Friend getFriend(int id) {
        Friend friend = null;
        final Cursor c = mGeoDatabase.getFriend(id);
        if (c != null) {
            if (c.moveToFirst())
//						a.friendid, a.address, a.parentid, a.title, a.descr, a.typeid, a.subtypeid, b.image
//				Friend(int id, int address, int parentid, String title, String descr, int typeid, int subtypeid, byte[] image)

                friend = new Friend(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6), c.getBlob(7));
            c.close();
        }

        return friend;
    }

    public void deletePoi(final int id){
        mGeoDatabase.deletePoi(id);
    }

    public void deletePoiSrcId(final int SrcId){
        mGeoDatabase.deletePoiSrcId(SrcId);
    }
    public void deletePoiSrcId2(final int SrcId){
        mGeoDatabase.deletePoiSrcId2(SrcId);
    }

    public void deletePoiCategory(final int id){
        mGeoDatabase.deletePoiCategory(id);
    }

    public PoiCategory getPoiCategory(int id) {
        PoiCategory category = null;
        final Cursor c = mGeoDatabase.getPoiCategory(id);
        if (c != null) {
            if (c.moveToFirst())
                category = new PoiCategory(id, c.getString(0), c.getInt(2) == ONE ? true : false, c.getInt(3), c.getInt(4));
            c.close();
        }

        return category;
    }

    public void updatePoiCategory(PoiCategory poiCategory) {
        if(poiCategory.getId() < ZERO)
            mGeoDatabase.addPoiCategory(poiCategory.Title, poiCategory.Hidden == true ? ONE : ZERO, poiCategory.IconId);
        else
            mGeoDatabase.updatePoiCategory(poiCategory.getId(), poiCategory.Title, poiCategory.Hidden == true ? ONE : ZERO, poiCategory.IconId, poiCategory.MinZoom);
    }

    public void updateFriendCategory(FriendCategory friendCategory) {
        if(friendCategory.getId() < ZERO)
            mGeoDatabase.addFriendCategory(friendCategory.Title, friendCategory.Hidden == true ? ONE : ZERO, friendCategory.IconId);
        else
            mGeoDatabase.updateFriendCategory(friendCategory.getId(), friendCategory.Title, friendCategory.Hidden == true ? ONE : ZERO, friendCategory.IconId, friendCategory.MinZoom);
    }

    public void DeleteAllPoi() {
        mGeoDatabase.DeleteAllPoi();
    }

    public void DeleteAllFriend() {
        mGeoDatabase.DeleteAllFriend();
    }

    public void deleteAllSkenario() {
        mGeoDatabase.deleteAllSkenario();
    }

    public void DeleteAllFoe() {
        mGeoDatabase.DeleteAllFoe();
    }

    public void beginTransaction(){
        mGeoDatabase.beginTransaction();
    }

    public void rollbackTransaction(){
        mGeoDatabase.rollbackTransaction();
    }

    public void commitTransaction(){
        mGeoDatabase.commitTransaction();
    }

    public void updateTrack(Track track) {
        if(track.getId() < 0){
            long newId = mGeoDatabase.addTrack(track.Name, track.Descr, track.Show ? ONE : ZERO, track.Cnt, track.Distance, track.Duration, track.Category, track.Activity, track.Date, track.Style);

            for(Track.TrackPoint trackpoint: track.getPoints()){
                mGeoDatabase.addTrackPoint(newId, trackpoint.lat, trackpoint.lon, trackpoint.alt, trackpoint.speed, trackpoint.date);
            }
        } else
            mGeoDatabase.updateTrack(track.getId(), track.Name, track.Descr, track.Show ? ONE : ZERO, track.Cnt, track.Distance, track.Duration, track.Category, track.Activity, track.Date, track.Style);
    }

    public void updateSkenario(Skenario skenario) {
        //if(skenario.getId() < 0){
        if(mGeoDatabase.isEmptySkn()){
            long newId = mGeoDatabase.addTrack(skenario.Name, skenario.Descr, skenario.Show ? ONE : ZERO, skenario.Cnt, skenario.Distance, skenario.Duration, skenario.Category, skenario.Activity, skenario.Date, skenario.Style);

            for(Skenario.SkenarioPoint skenariopoint: skenario.getPoints()){
                mGeoDatabase.addSkenarioPoint(newId, skenariopoint.lat, skenariopoint.lon, skenariopoint.alt, skenariopoint.speed, skenariopoint.date);
            }
        } else
            //mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, skenario.Show ? ONE : ZERO, skenario.Cnt, skenario.Distance, skenario.Duration, skenario.Category, skenario.Activity, skenario.Date, skenario.Style);
            mGeoDatabase.updateSkenario(skenario.getId(), skenario.Name, skenario.Descr, skenario.Show ? ONE : ZERO, skenario.Cnt, skenario.Distance, skenario.Duration, skenario.Category, skenario.Activity, skenario.Date, skenario.Style, skenario.Skenariosrcid);
    }

    public void updateKendali(Kendali kendali) {
        if(kendali.getId() < 0){
            long newId = mGeoDatabase.addTrack(kendali.Name, kendali.Descr, kendali.Show ? ONE : ZERO, kendali.Cnt, kendali.Distance, kendali.Duration, kendali.Category, kendali.Activity, kendali.Date, kendali.Style);

            for(Kendali.KendaliPoint kendalipoint: kendali.getPoints()){
                mGeoDatabase.addTrackPoint(newId, kendalipoint.lat, kendalipoint.lon, kendalipoint.alt, kendalipoint.speed, kendalipoint.date);
            }
        } else
            mGeoDatabase.updateTrack(kendali.getId(), kendali.Name, kendali.Descr, kendali.Show ? ONE : ZERO, kendali.Cnt, kendali.Distance, kendali.Duration, kendali.Category, kendali.Activity, kendali.Date, kendali.Style);
    }

    public boolean haveTrackChecked(){
        boolean ret = false;
        Cursor c = mGeoDatabase.getTrackChecked();
        if (c != null) {
            if (c.moveToFirst())
                ret = true;
            c.close();
        }

        return ret;
    }

    public boolean haveKendaliChecked(){
        boolean ret = false;
        Cursor c = mGeoDatabase.getKendaliChecked();
        if (c != null) {
            if (c.moveToFirst())
                ret = true;
            c.close();
        }

        return ret;
    }

    public Track[] getTrackChecked(){
        return getTrackChecked(true);
    }

    public Kendali[] getKendaliChecked(){
        return getKendaliChecked(true);
    }

    public Skenario[] getSkenarioChecked(){
        return getSkenarioChecked(true);
    }


    public Track[] getTrackChecked(final boolean aNeedPoints){
        mStopProcessing = false;
        Track tracks[] = null;
        Cursor c = mGeoDatabase.getTrackChecked();
        if (c != null) {
            tracks = new Track[c.getCount()];
            final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");

            if (c.moveToFirst())
                do {
                    final int pos = c.getPosition();
                    String style = c.getString(10);
                    if(style == null || style.equalsIgnoreCase(""))
                        style = "";

                    tracks[pos] = new Track(c.getInt(3), c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(4), c.getDouble(5), c.getDouble(6), c.getInt(7), c.getInt(8), new Date(c.getLong(9)*1000), style, defStyle);

                    if (aNeedPoints) {
                        Cursor cpoints = mGeoDatabase.getTrackPoints(tracks[pos].getId());
                        if (cpoints != null) {
                            if (cpoints.moveToFirst()) {
                                do {
                                    if (Stop()) {
                                        tracks[pos] = null;
                                        break;
                                    }
                                    tracks[pos].AddTrackPoint(); //track.trackpoints.size()
                                    tracks[pos].LastTrackPoint.lat = cpoints.getDouble(0);
                                    tracks[pos].LastTrackPoint.lon = cpoints.getDouble(1);
                                    tracks[pos].LastTrackPoint.alt = cpoints.getDouble(2);
                                    tracks[pos].LastTrackPoint.speed = cpoints.getDouble(3);
                                    tracks[pos].LastTrackPoint.date.setTime(cpoints.getLong(4) * 1000); // System.currentTimeMillis()
                                } while (cpoints.moveToNext());
                            }
                            cpoints.close();
                        }
                    }
                } while (c.moveToNext());
            else {
                c.close();
                return null;
            }
            c.close();


        }
        return tracks;
    }

    public Kendali[] getKendaliChecked(final boolean aNeedPoints){
        mStopProcessing = false;
        Kendali kendali[] = null;
        Cursor c = mGeoDatabase.getKendaliChecked();
        if (c != null) {
            kendali = new Kendali[c.getCount()];
            final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");

            if (c.moveToFirst())
                do {
                    final int pos = c.getPosition();
                    String style = c.getString(10);
                    if(style == null || style.equalsIgnoreCase(""))
                        style = "";

                    kendali[pos] = new Kendali(c.getInt(3), c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(4), c.getDouble(5), c.getDouble(6), c.getInt(7), c.getInt(8), new Date(c.getLong(9)*1000), style, defStyle);

                    if (aNeedPoints) {
                        Cursor cpoints = mGeoDatabase.getKendaliPoints(kendali[pos].getId());
                        if (cpoints != null) {
                            if (cpoints.moveToFirst()) {
                                do {
                                    if (Stop()) {
                                        kendali[pos] = null;
                                        break;
                                    }
                                    kendali[pos].AddKendaliPoint();
                                    kendali[pos].LastKendaliPoint.lat = cpoints.getDouble(0);
                                    kendali[pos].LastKendaliPoint.lon = cpoints.getDouble(1);
                                    kendali[pos].LastKendaliPoint.alt = cpoints.getDouble(2);
                                    kendali[pos].LastKendaliPoint.speed = cpoints.getDouble(3);
                                    kendali[pos].LastKendaliPoint.date.setTime(cpoints.getLong(4) * 1000);
                                } while (cpoints.moveToNext());
                            }
                            cpoints.close();
                        }
                    }
                } while (c.moveToNext());
            else {
                c.close();
                return null;
            }
            c.close();
        }
        return kendali;
    }

    public Skenario[] getSkenarioChecked(final boolean aNeedPoints){
        mStopProcessing = false;
        Skenario skenario[] = null;
        Cursor c = mGeoDatabase.getSkenarioChecked();
        if (c != null) {
            skenario = new Skenario[c.getCount()];
            final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");

            if (c.moveToFirst())
                do {
                    final int pos = c.getPosition();
                    String style = c.getString(10);
                    if(style == null || style.equalsIgnoreCase(""))
                        style = "";

                    skenario[pos] = new Skenario(c.getInt(3), c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(4), c.getDouble(5), c.getDouble(6), c.getInt(7), c.getInt(8), new Date(c.getLong(9)*1000), style, defStyle, c.getInt(11));

                    if (aNeedPoints) {
                        Cursor cpoints = mGeoDatabase.getSkenarioPoints(skenario[pos].getId());
                        if (cpoints != null) {
                            if (cpoints.moveToFirst()) {
                                do {
                                    if (Stop()) {
                                        skenario[pos] = null;
                                        break;
                                    }
                                    skenario[pos].AddSkenarioPoint();
                                    skenario[pos].LastSkenarioPoint.lat = cpoints.getDouble(0);
                                    skenario[pos].LastSkenarioPoint.lon = cpoints.getDouble(1);
                                    skenario[pos].LastSkenarioPoint.alt = cpoints.getDouble(2);
                                    skenario[pos].LastSkenarioPoint.speed = cpoints.getDouble(3);
                                    skenario[pos].LastSkenarioPoint.date.setTime(cpoints.getLong(4) * 1000);
                                } while (cpoints.moveToNext());
                            }
                            cpoints.close();
                        }
                    }
                } while (c.moveToNext());
            else {
                c.close();
                return null;
            }
            c.close();
        }
        return skenario;
    }

    public Track getTrack(int id){
        Track track = null;
        Cursor c = mGeoDatabase.getTrack(id);
        if (c != null) {
            if (c.moveToFirst()) {
                final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");
                String style = c.getString(9);
                if(style == null || style.equalsIgnoreCase(""))
                    style = "";

                track = new Track(id, c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(3), c.getDouble(4), c.getDouble(5), c.getInt(6), c.getInt(7), new Date(c.getLong(8)*1000), style, defStyle);
            };
            c.close();
            c = null;

            c = mGeoDatabase.getTrackPoints(id);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        track.AddTrackPoint();
                        track.LastTrackPoint.lat = c.getDouble(0);
                        track.LastTrackPoint.lon = c.getDouble(1);
                        track.LastTrackPoint.alt = c.getDouble(2);
                        track.LastTrackPoint.speed = c.getDouble(3);
                        track.LastTrackPoint.date.setTime(c.getLong(4) * 1000); // System.currentTimeMillis()
                    } while (c.moveToNext());
                }
                c.close();
            }

        }

        return track;
    }

    public ArrayList<String> getListSkenario(){
        ArrayList<String> arrayListSkenario = new ArrayList<>();
        //String tempDataLatLon = "";
        List tempDataLatLon = new ArrayList();
        Cursor c = mGeoDatabase.getListSkenario();
        String tempPSkenarioId = "",tempSkenarioSrcId ="", tempName="", tempDesc="";
        String pSkenarioPointId = "", pSkenarioId = "", descr="", skenarioSrcId="", date = "";
        int categoryId = 0, cnt = 0;
        String name = "";
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                pSkenarioId = c.getString(c.getColumnIndex("pskenarioid"));
                pSkenarioPointId = c.getString(c.getColumnIndex("id"));
                name = c.getString(c.getColumnIndex("name"));
                descr = c.getString(c.getColumnIndex("descr"));
                date = c.getString(c.getColumnIndex("date"));
                cnt = Integer.parseInt(c.getString(c.getColumnIndex("cnt")));
                categoryId = Integer.parseInt(c.getString(c.getColumnIndex("categoryid")));
                skenarioSrcId = c.getString(c.getColumnIndex("skenariosrcid"));
                Double lat = Double.parseDouble(c.getString(c.getColumnIndex("lat")));
                Double lon = Double.parseDouble(c.getString(c.getColumnIndex("lon")));
                Log.d("DBManager Skenario", "pskenarioid:" + pSkenarioId + "-tempPSkenarioId:" + tempPSkenarioId + "-pSkenarioPointId:" + pSkenarioPointId + "-skenariosrcid:" + skenarioSrcId + "-lat:" + lat + "-lon:" + lon);

                if (tempPSkenarioId==""){
                    tempPSkenarioId = pSkenarioId;
                    tempSkenarioSrcId = skenarioSrcId;
                    tempName = name;
                    tempDesc = descr;
                }

                if (tempPSkenarioId.equalsIgnoreCase(pSkenarioId)){
                    tempDataLatLon.add(lat);
                    tempDataLatLon.add(lon);
                    //tempDataLatLon+=lat + "," + lon + ",";
                }else{
                    Log.d(TAG, "TextUtils.join:" + TextUtils.join(",",tempDataLatLon));
                    arrayListSkenario.add(tempPSkenarioId + "," + pSkenarioPointId + ","
                            + tempName + "," + tempDesc + ","  + categoryId  + "," + tempSkenarioSrcId + ","
                            + TextUtils.join(",",tempDataLatLon));
                    tempDataLatLon.clear();
                    tempPSkenarioId = pSkenarioId;
                    tempSkenarioSrcId = skenarioSrcId;
                    tempName = name;
                    tempDesc = descr;
                    tempDataLatLon.add(lat);
                    tempDataLatLon.add(lon);
                }
                c.moveToNext();
            }
            arrayListSkenario.add(tempPSkenarioId + "," + pSkenarioPointId + ","
                    + name + "," + descr + ","  + categoryId  + "," + skenarioSrcId + ","
                    + TextUtils.join(",",tempDataLatLon));
        }
        c.close();
        return arrayListSkenario;
    }

    public int insertSkenario(String name, String descr, String date, int cnt, int categoryId, String skenarioSrcId){
      int pSkenarioId  = (int) mGeoDatabase.insertSkenario(name, descr, date,
              "5", cnt, "", "",
              categoryId, "", "", skenarioSrcId);
      Log.d(TAG, "insertSkenario:" + pSkenarioId);
      return pSkenarioId;
    }

    public String insertSkenarioPoints(String[] skenarioArrParsed, ArrayList<Double> geoPoints){

        //004,65535,38,2,Ãbbb,1,desc2,4,-6.974234,107.30484,-6.800572,107.381504,-6.7558,107.29252,-6.711028,107.3596
        String name = skenarioArrParsed[0];
        String senderId = skenarioArrParsed[2];
        String indexer = skenarioArrParsed[3];
        String desc = skenarioArrParsed[4];
        String date = "";
        int categoryid = Integer.parseInt(skenarioArrParsed[7]);
        int cnt =  geoPoints.size();
        String skenarioSrcId = indexer + "~" + senderId;
        int pskenarioid = this.insertSkenario(name, desc, date, cnt, categoryid, skenarioSrcId);

        for(int i=0;i<cnt; i+=2){
            mGeoDatabase.insertSkenarioPoints(pskenarioid, geoPoints.get(i), geoPoints.get(i+1), "", "", "");
        }

        return skenarioSrcId;

    }

    public void deleteSkenarioById(int pSkenarioId){
        Log.d(TAG, "deleteSkenarioById");
        mGeoDatabase.deleteSkenarioById(pSkenarioId);
    }

    public Bitmap getTacticalSymbol(String idImage){
        Cursor c = mGeoDatabase.getTacticalSymbol(idImage);
        Bitmap bp = null;
        if (c.moveToFirst()) {
            String symId = c.getString(c.getColumnIndex("symid"));
            String categoryId = c.getString(c.getColumnIndex("categoryid"));
            String descr = c.getString(c.getColumnIndex("descr"));
            byte[] image = c.getBlob(c.getColumnIndex("image"));
            Log.d(TAG, "getTacticalSymbol:" + symId + "-" + categoryId + "-" + descr + "-" + image);
            bp= BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return bp;
    }

    public Skenario getSkenario(int id){
        Skenario skenario = null;
        Cursor c = mGeoDatabase.getSkenario(id);
        if (c != null) {
            if (c.moveToFirst()) {
                final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");
                String style = c.getString(9);
                if(style == null || style.equalsIgnoreCase(""))
                    style = "";

                skenario = new Skenario(id, c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(3), c.getDouble(4), c.getDouble(5), c.getInt(6), c.getInt(7), new Date(c.getLong(8)*1000), style, defStyle, c.getInt(10));
            };
            c.close();
            c = null;

            c = mGeoDatabase.getSkenarioPoints(id);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        skenario.AddSkenarioPoint();
                        skenario.LastSkenarioPoint.lat = c.getDouble(0);
                        skenario.LastSkenarioPoint.lon = c.getDouble(1);
                        skenario.LastSkenarioPoint.alt = c.getDouble(2);
                        skenario.LastSkenarioPoint.speed = c.getDouble(3);
                        skenario.LastSkenarioPoint.date.setTime(c.getLong(4) * 1000); // System.currentTimeMillis()
                    } while (c.moveToNext());
                }
                c.close();
            }

        }

        return skenario;
    }



    public Kendali getKendali(int id){
        Kendali kendali = null;
        Cursor c = mGeoDatabase.getKendali(id);
        if (c != null) {
            if (c.moveToFirst()) {
                final String defStyle = PreferenceManager.getDefaultSharedPreferences(mCtx).getString("pref_track_style", "");
                String style = c.getString(9);
                if(style == null || style.equalsIgnoreCase(""))
                    style = "";

                kendali = new Kendali(id, c.getString(0), c.getString(1), c.getInt(2) == ONE ? true : false, c.getInt(3), c.getDouble(4), c.getDouble(5), c.getInt(6), c.getInt(7), new Date(c.getLong(8)*1000), style, defStyle);
            };
            c.close();
            c = null;

            c = mGeoDatabase.getKendaliPoints(id);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        kendali.AddKendaliPoint();
                        kendali.LastKendaliPoint.lat = c.getDouble(0);
                        kendali.LastKendaliPoint.lon = c.getDouble(1);
                        kendali.LastKendaliPoint.alt = c.getDouble(2);
                        kendali.LastKendaliPoint.speed = c.getDouble(3);
                        kendali.LastKendaliPoint.date.setTime(c.getLong(4) * 1000);
                    } while (c.moveToNext());
                }
                c.close();
            }
        }

        return kendali;
    }

    public void setTrackChecked(int id) {
        mGeoDatabase.setTrackChecked(id);
    }

    public void setKendaliChecked(int id) {
        mGeoDatabase.setKendaliChecked(id);
    }

    public void deleteTrack(int id) {
        mGeoDatabase.deleteTrack(id);
    }

    public void deleteSkenario(int id) {
        mGeoDatabase.deleteSkenario(id);
    }

    public void deleteKendali(int id) {
        mGeoDatabase.deleteKendali(id);
    }

    public void deleteCuaca(){
        mGeoDatabase.deleteAllCuaca();
    }

    public long addMap(int type, String params) {
        return mGeoDatabase.addMap(type, params);
    }

    public String[] getTrackPointString() {
        String[] strLoc = null;
        Cursor c = mGeoDatabase.getTP();
        if(c != null) {
            strLoc = new String[c.getCount()];
            if(c.moveToFirst())
                do {
                    final int pos = c.getPosition();
                    strLoc[pos] = c.getDouble(0) + "," + c.getDouble(1);
                } while(c.moveToNext());
        }
        return strLoc;
    }

    public String[] getKendaliPointString() {
        String[] strLoc = null;
        Cursor c = mGeoDatabase.getKP();
        if(c != null) {
            strLoc = new String[c.getCount()];
            if(c.moveToFirst())
                do {
                    final int pos = c.getPosition();
                    strLoc[pos] = c.getDouble(0) + "," + c.getDouble(1);
                } while(c.moveToNext());
        }
        return strLoc;
    }

    public byte[] getTacticalSymbolImage(final int symid) {
        Cursor c = mGeoDatabase.getTacticalSymbolImage(symid);
        byte[] img = null;
        if(c != null) {
            if(c.moveToFirst())
                do {
                    img = c.getBlob(4);
                } while(c.moveToNext());
        }
        return img;
    }

    public RoomChat getRoomChat(int id) {
        RoomChat roomChat = null;
        final Cursor c = mGeoDatabase.getRoomChat(id);
        if (c != null) {
            if (c.moveToFirst()) {
                roomChat = new RoomChat(c.getInt(0), c.getString(1),c.getInt(2), c.getInt(3), c.getInt(4));
            }
            c.close();
        }

        return roomChat;
    }

    public void updateChatRcvDate(final String aSenddate, final String aReceivedate) {
        mGeoDatabase.updateChatRcvDate(aSenddate, aReceivedate);
    }

    public void updateChatIsRead(int roomchatid) {
        //set isread = 1

        Log.d(TAG, "roomchatid: "+roomchatid);
        mGeoDatabase.updateChatIsRead(roomchatid);
    }

    public void deleteRoomChat(final int id){
        mGeoDatabase.deleteRoomchat(id);
        mGeoDatabase.clearChat(id);
    }

    public void clearChat(final int roomchatid) {
        mGeoDatabase.clearChat(roomchatid);
    }

    public void updateRoomChat(final RoomChat roomChat, boolean isNew){
        int updateCount = 0;
        if(isNew)
            mGeoDatabase.addRoomchat(roomChat.Id, roomChat.Name, roomChat.Src, roomChat.Dst, roomChat.Ismaster == true ? ONE : ZERO);
        else
            updateCount = mGeoDatabase.updateRoomchat(roomChat.Id, roomChat.Name, roomChat.Src, roomChat.Dst, roomChat.Ismaster == true ? ONE : ZERO);
        int ismaster = roomChat.Ismaster == true ? ONE : ZERO;
        Log.d(TAG, "roomChat: "+roomChat.Id+"/"+roomChat.Name+"/"+roomChat.Src+"/"+roomChat.Dst+"/"+ismaster);
        Log.d(TAG, "updateCount: "+updateCount);
    }

//	public int getDeviceId() {
//		final Cursor c = mGeoDatabase.getMyIdentityCursor();
//		if (c != null) {
//			if (c.moveToFirst())
//				return c.getInt(0);
//			c.close();
//		}
//
//		return 0;
//	}

    public void updateLockMsh(int id, int symbolId) {
        mGeoDatabase.updateSymbolMsh(id, symbolId);
    }

    public void updateFriendLoc(int address, double lat, double lon, float heading1, float heading2) {
        mGeoDatabase.updateFriendLoc(address, lat, lon, heading1, heading2);
    }

    public void updateFriendPar(int address, double bbLevel, int ammoRest) {
        //mGeoDatabase.updateFriendLoc(address, lat, lon, heading1, heading2);
    }

    public void updateCuaca(int id, double suhu, double kelembaban, double hujan, double kabut, double angin) {
        mGeoDatabase.updateCuaca(id, suhu, kelembaban, hujan, kabut, angin);
    }


    public FriendCategory getFriendCategory(int id) {
        FriendCategory category = null;
        final Cursor c = mGeoDatabase.getFriendCategory(id);
        if (c != null) {
            if (c.moveToFirst())
                category = new FriendCategory(id, c.getString(0), c.getInt(2) == ONE ? true : false, c.getInt(3), c.getInt(4));
            c.close();
        }

        return category;
    }

    public void deleteFriend(int friendid) {
        mGeoDatabase.deleteFriend(friendid);
    }

    public void updateFriend(final Friend friend) {
//		long newId = -1;
//		if(friend.getId() < 0)
//			newId = mGeoDatabase.addFriend(friend.Address, friend.Parentid, friend.CategoryId, friend.SymbolId, friend.Title, friend.Descr);
//		else
        mGeoDatabase.updateFriend(friend.getId(), friend.Address, friend.ParentId, friend.TypeId, friend.SubtypeId, friend.Title, friend.Descr);

//		return newId;
    }

    public Identity getIdentity() {
        Identity identity = null;
        final Cursor c = mGeoDatabase.getMyOverlayCursor();
        //final Cursor c = mGeoDatabase.getMyIdentityCursor();
        if (c != null) {
            if (c.moveToFirst())
                identity = new Identity(c.getInt(0), c.getString(1), c.getBlob(2), c.getInt(3), c.getInt(4));

            c.close();
        }

        return identity;
    }
}
