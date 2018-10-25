package com.bms.user.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.bms.user.model.ModelSetIp;
import com.bms.user.model.ModelSettingSmartwatch;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.interfaces.PBEKey;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public class DbManager {
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

    private void startManagingCursor(Cursor cur) {
        // TODO Auto-generated method stub

    }

    public ArrayList<String> getListSkenario() {
        ArrayList<String> arrayListSkenario = new ArrayList<>();
        //String tempDataLatLon = "";
        List tempDataLatLon = new ArrayList();
        Cursor c = mGeoDatabase.getListSkenario();
        String tempPSkenarioId = "", tempSkenarioSrcId = "", tempName = "", tempDesc = "";
        String pSkenarioPointId = "", pSkenarioId = "", descr = "", skenarioSrcId = "", date = "";
        int categoryId = 0, cnt = 0;
        String name = "";
        if (c != null) {
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

                    if (tempPSkenarioId == "") {
                        tempPSkenarioId = pSkenarioId;
                        tempSkenarioSrcId = skenarioSrcId;
                        tempName = name;
                        tempDesc = descr;
                    }

                    if (tempPSkenarioId.equalsIgnoreCase(pSkenarioId)) {
                        tempDataLatLon.add(lat);
                        tempDataLatLon.add(lon);
                        //tempDataLatLon+=lat + "," + lon + ",";
                    } else {
                        Log.d(TAG, "TextUtils.join:" + TextUtils.join(",", tempDataLatLon));
                        arrayListSkenario.add(tempPSkenarioId + "," + pSkenarioPointId + ","
                                + tempName + "," + tempDesc + "," + categoryId + "," + tempSkenarioSrcId + ","
                                + TextUtils.join(",", tempDataLatLon));
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
                    + name + "," + descr + "," + categoryId + "," + skenarioSrcId + ","
                    + TextUtils.join(",", tempDataLatLon));
            }
            c.close();
        }

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

    public int updateSettingSmartwatch(ModelSettingSmartwatch modelSettingSmartwatch){

        int res = (int) mGeoDatabase.updateSettingSmartwatch(modelSettingSmartwatch);
        return res;
    }

    public int updateSetIp(String SSIDName, String SSIDPassword, String myIpAddress,
                                       String myGateway){

        int res = (int) mGeoDatabase.updateSetIp(
                SSIDName, SSIDPassword, myIpAddress, myGateway
        );
        return res;
    }

    public ModelSetIp getWifiConfig(){
        Cursor c = mGeoDatabase.getWifiConfig();
        ModelSetIp modelSetIp = new ModelSetIp();
        if (c.moveToFirst()) {
            modelSetIp.setSSIDName(c.getString(c.getColumnIndex("ssid_name")));
            modelSetIp.setSSIDPassword(c.getString(c.getColumnIndex("ssid_password")));
            modelSetIp.setMyIp(c.getString(c.getColumnIndex("my_ip_address")));
            modelSetIp.setMyGateway(c.getString(c.getColumnIndex("my_gateway")));

            Log.d(TAG, "getModelSetIp:" + modelSetIp + "-" + modelSetIp.getSSIDName());
        }
        return modelSetIp;
    }

    public ModelSettingSmartwatch getSettingSmartwatch(){
        Cursor c = mGeoDatabase.getSettingSmartwatch();
        ModelSettingSmartwatch modelSettingSmartwatch = new ModelSettingSmartwatch();
        if (c != null) {
            if (c.moveToFirst()) {
                modelSettingSmartwatch.setSendTo(c.getString(c.getColumnIndex("send_to")));
                modelSettingSmartwatch.setMapsName(c.getString(c.getColumnIndex("maps_name")));
                modelSettingSmartwatch.setMapsPath(c.getString(c.getColumnIndex("maps_path")));
                modelSettingSmartwatch.setGroup(c.getString(c.getColumnIndex("set_group")));
                modelSettingSmartwatch.setIpCCU(c.getString(c.getColumnIndex("ip_ccu")));
                modelSettingSmartwatch.setIpDriverView(c.getString(c.getColumnIndex("ip_driverview")));
                modelSettingSmartwatch.setSdrLat(c.getString(c.getColumnIndex("sdr_lat")));
                modelSettingSmartwatch.setSdrLon(c.getString(c.getColumnIndex("sdr_lon")));
            }
        }
        return modelSettingSmartwatch;
    }

    public int clearAll(){
        int res = (int) mGeoDatabase.clearAll();
        return res;
    }


    public int insertTacticalSymbol(String name, String descr, Double lat, Double lon,
                                    Double alt, int hidden, int categoryid, int pointsourceid,
                                    int iconid){
        int pSituasi  = (int) mGeoDatabase.insertTacticalSymbol(name, descr, lat, lon, alt,
                hidden, categoryid, pointsourceid, iconid);
        return pSituasi;
    }

    public void deleteSkenarioById(int pSkenarioId){
        Log.d(TAG, "deleteSkenarioById");
        mGeoDatabase.deleteSkenarioById(pSkenarioId);
    }

    public Bitmap getTacticalSymbol(int idImage){
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

}
