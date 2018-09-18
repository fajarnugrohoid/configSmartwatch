package com.bms.user.bmssmartwatch;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RotaryEncoder;
import android.support.wearable.view.BoxInsetLayout;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;

import message.EventMessage;
import services.DriverViewService;
import shape.ShapeIcon;
import shape.ShapeOverlay;
import shape.TLabelAlignType;
import shape.TShapeImage;
import shape.TShapeLabel;
import shape.TShapeOverlay;
import model.ModelMessage;
import shape.TShapePolyline;

public class MainActivity extends WearableActivity implements MapListener, MapEventsReceiver{
//public class MainActivity extends WearableActivity implements MapListener{
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    FolderOverlay activeLatLonGrid;

    public Context context;

    public static MapView map = null;
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    public String TAG = "smartwatch";
    public static boolean driverView = false;

    @Override
    public  boolean onScroll(ScrollEvent scrollEvent)
    {
        return  true;
    }

    @Override
    public  boolean onZoom(ZoomEvent zoomEvent)
    {
        return  false;
    }

    public static File OSMDROID_PATH = new File(Environment.getExternalStorageDirectory(),
            "osmdroid");
    boolean isGridVisible = true;

    private Polyline polyline;
    ArrayList<OverlayItem> overlayItemArray;

    Drawable markerTP, myMarker;
    public static IMapController mapController;
    OverlayItem myCurrentPosition;
    public static ShapeOverlay lyr, lyrChild;
    public static TShapeOverlay lyrObjectSituation, lyrLocal, lyrBcast, lyrNeighbour, lyrTactical, lyrKawan;
    Location loc,locChild;
    Bitmap bmp, bmpChild, bmpChild2;
    TShapeImage shpImage;
    ShapeIcon icon, iconChild;
    List<ShapeIcon> shapeIconsList = new ArrayList<ShapeIcon>();
    List<Location> locList = new ArrayList<Location>();

    List<TShapeImage> tShapeImagesList = new ArrayList<TShapeImage>();
    List<TShapeImage> tBcastShapeImagesList = new ArrayList<TShapeImage>();
    List<TShapeImage> tNeighbourShapeImagesList = new ArrayList<TShapeImage>();
    Canvas canvas = new Canvas();
    Paint paint = new Paint();
    public static ImageView ic_disconnect, ivTacticalSymbol;
    DbManager dbManager;

    TShapeLabel dataLabel;
    public static TShapeImage tImageObject,tBcastImageObject, tNeighbourImageObject;

    final Context c = this;
    private static final String LOG_TAG = "smartwatch-driverview";

    Animation animation = new AlphaAnimation((float) 0.5, 0);
    TextView textViewSMS;

    SMSFragment smsFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        //mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        //mTextView = (TextView) findViewById(R.id.text);
        //mClockView = (TextView) findViewById(R.id.text);



        //register EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        map = (MapView) findViewById(R.id.map);
        Context ctx = getApplicationContext();

        isStoragePermissionGranted();
        //isChangeWifiStatePermissionGranted();

        map.setUseDataConnection(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMapListener(this);
        map.setMinZoomLevel(9);
        map.setMaxZoomLevel(23);

        String imgPath = android.os.Environment.getExternalStorageDirectory().toString() + "/Download/Cipatat/Cipatat/Topografi";
        //String imgPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Download/HariffDTE/HariffDTE";
        //String imgPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Download/mabes/Googlemap";
        AssetsTileSource source = new AssetsTileSource(null, imgPath, 10, 21, 256, ".png");
        //AssetsTileSource source = new AssetsTileSource(null,imgPath,17,21,256,".jpg");
        MapTileModuleProviderBase moduleProvider = new MapTileFileAssetsProvider(source);
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(getApplicationContext());
        MapTileProviderArray tileProviderArray = new MapTileProviderArray(source, simpleReceiver, new MapTileModuleProviderBase[]{moduleProvider});
        map.setTileProvider(tileProviderArray);

        mapController = map.getController();
        mapController.setZoom(11);
        //mapController.setZoom(19);
        Double latMabes = -6.169915;
        Double lonMabes = 106.830049;
        Double latCipatat = -6.839806;
        Double lonCipatat = 107.385608;
        GeoPoint point = new GeoPoint(latCipatat, lonCipatat);
        //GeoPoint point = new GeoPoint(-6.949630, 107.623169);
        mapController.setCenter(point);

        lyrLocal = new TShapeOverlay(this, "tshapeoverlaymain");
        lyrBcast = new TShapeOverlay(this, "tshapeoverlaybcast");
        lyrNeighbour = new TShapeOverlay(this, "tshapeoverlayneighbour");
        lyrObjectSituation = new TShapeOverlay(this, "tshapeoverlaychild");
        lyrTactical = new TShapeOverlay(this, "tshapeoverlaytactical");
        lyrKawan = new TShapeOverlay(this, "tshapeoverlaykawan");

        map.setMapListener(this);

        ic_disconnect = new ImageView(this);
        ic_disconnect = (ImageView) findViewById(R.id.ic_disconnect);
        ivTacticalSymbol = new ImageView(this);
        ivTacticalSymbol = (ImageView) findViewById(R.id.ivTacticalSymbol);

        /*
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        GeoPoint pointx;
        String xxx = "-6.8441,107.423992,-6.85521,107.409336,-6.851565,107.397472,-6.834579, 107.398906";
        String[] xxxArr = xxx.split(",",-1);
        for (int i =0; i < xxxArr.length; i+=2) {
            pointx = new GeoPoint(Double.parseDouble(xxxArr[i]), Double.parseDouble(xxxArr[i + 1]));
            Log.i("slopDegree Main", pointx.getLatitude() + "," + pointx.getLongitude());
            geoPoints.add(pointx);
        }

        TShapePolyline tShapePolyline = new TShapePolyline();
        tShapePolyline.setColor(Color.RED);
        tShapePolyline.setShapeName("tes");
        tShapePolyline.setIsRute(true);
        tShapePolyline.setLocations(geoPoints);
        //tShapePolyline.setDashed(true);

        lyrLocal.Shapes.add(tShapePolyline); */

        map.getOverlays().add(lyrLocal);
        map.getOverlays().add(lyrBcast);
        map.getOverlays().add(lyrNeighbour);
        map.getOverlays().add(lyrObjectSituation);
        map.getOverlays().add(lyrTactical);

        map.invalidate();

        Intent driverViewService = new Intent(this, DriverViewService.class);
        startService(driverViewService);

        /*
        Intent intent = new Intent(this, LocalService.class);
        startService(intent);

        Intent childService = new Intent(this, ChildService.class);
        startService(childService);

        Intent neighbourService = new Intent(this, NeighbourService.class);
        startService(neighbourService);

        Intent broadcastService = new Intent(this, BroadcastService.class);
        startService(broadcastService); */


        dbManager = new DbManager(ctx);

        setLoadFromDB();

        ModelMessage mqLocal = new ModelMessage(0, "", "", 0,latCipatat, lonCipatat, "");
        ModelMessage mqFriend = new ModelMessage(0, "", "", 0,latCipatat, lonCipatat, "");

        this.createNewTileLocalPosition(mqLocal);
        //this.createNewTileFriendPosition(mqFriend, 0, lyrKawan);
        deleteLyrKawan();


        Button btn;

        smsFragment=new SMSFragment();
        fragmentManager = getFragmentManager();

//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragmentSMS, smsFragment, "textMessage");
//        transaction.commit();

        /*btn=(Button) findViewById(R.id.button1);
        smsFragment.setValue("SMS XXX");
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                smsFragment.show(fragmentManager, "SMS");
            }
        });*/

        //showToastMessage("test sms");

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
            // Note that we negate the delta value here in order to get the right scroll direction.
            float delta = -RotaryEncoder.getRotaryAxisValue(ev)
                    * RotaryEncoder.getScaledScrollFactor(getApplicationContext());
            //scrollBy(0, Math.round(delta));
            Log.d(TAG, "rotary input up delta:" + delta + "-math round:" + Math.round(delta));
            Log.d(TAG, "map.getZoomLevel:" + map.getZoomLevel());
            if (Math.round(delta)>2){
                if (map.getZoomLevel()<=21){
                    mapController.zoomIn();
                }else{
                    return false;
                }
            }else if (Math.round(delta)<-2) {
                if (map.getZoomLevel()>=11){
                    mapController.zoomOut();
                }else{
                    return false;
                }
            }
            return true;
        }
        return super.onGenericMotionEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setLoadFromDB(){
        ArrayList<String> arrayListSkenario = dbManager.getListSkenario();
        Log.d(TAG, "arrayListSkenario.size:" + arrayListSkenario.size() + "--" + arrayListSkenario);
        if (arrayListSkenario.size()> 0){
            for (int j = 0; j < arrayListSkenario.size(); j++) {
                Log.d(TAG, "arrayListSkenario:" + arrayListSkenario.get(j));

                String[] skenarioArrParsed = arrayListSkenario.get(j).split(",", -1);
                Log.d(LOG_TAG, "getmessage skenario :" + skenarioArrParsed[1]);

                ArrayList<GeoPoint> geoPoints = new ArrayList<>();
                GeoPoint pointDb;
                if (skenarioArrParsed.length > 0) {
                    for (int i = 6; i < skenarioArrParsed.length; i += 2) {
                        Log.d(LOG_TAG, "getmessage skenario lat:" + i + ":" + skenarioArrParsed[i]);
                        Log.d(LOG_TAG, "getmessage skenario long:" + i + "+1:" + skenarioArrParsed[i + 1]);
                        pointDb = new GeoPoint(Double.parseDouble(skenarioArrParsed[i]), Double.parseDouble(skenarioArrParsed[i + 1]));
                        geoPoints.add(pointDb);
                    }
                }
                String skenarioSrcId = skenarioArrParsed[5];
                Log.d(TAG, "skenarioId:" + skenarioArrParsed[0] + "-skenarioSrcId:" + skenarioSrcId);
                TShapePolyline tShapePolyline = new TShapePolyline();
                tShapePolyline.setColor(Color.RED);
                tShapePolyline.setShapeName(skenarioSrcId);
                tShapePolyline.setIsRute(true);
                //int indexer = Integer.parseInt(1);
                tShapePolyline.setID(Integer.parseInt(skenarioArrParsed[0]));
                tShapePolyline.setLocations(geoPoints);
                lyrTactical.Shapes.add(tShapePolyline);
                map.getOverlays().add(lyrTactical);
                map.invalidate();
            }
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Storage Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Storage Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Storage Permission is granted");
            return true;
        }
    }

    public  boolean isChangeWifiStatePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Change Wifi State Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Change Wifi State Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Change Wifi State Permission is granted");
            return true;
        }
    }

    public  boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Fine Location Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Fine Location Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Fine Location Permission is granted");
            return true;
        }
    }

    @Subscribe
    public void onEvent(EventMessage event)
    {
        ModelMessage mq = event.getMessageQueue();
        Log.i(TAG, "EventBus:" + mq.getMessage());
        Log.i(TAG, "getPortName:" + mq.getPortName());

        if (mq.getPortName().equalsIgnoreCase("local")){
            Log.i(TAG, "EventBus Local:" + mq.getMessage());
            if (mq.getAddrId().equalsIgnoreCase("000")) {
                int count = lyrLocal.Shapes.size();
                Log.i(TAG, "Lat Long Current Position Local : " + mq.getLat() + "," + mq.getLong() + "-count:" + count);
                if (mq.getLat()!=0.0 && mq.getLong()!=0.0){
                    if (count <= 0){
                        //create new
                        Log.i(TAG, "create new local count:" + count);
                        /*dataLabel = new TShapeLabel();
                        dataLabel.Text = "sdr";
                        dataLabel.AlignType = TLabelAlignType.RightCenter;
                        TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                        GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

                        tImageObject = new TShapeImage();
                        tImageObject.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.drawable_position_blue_small));
                        tImageObject.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baloontips_blue_normal_right));
                        tImageObject.setLocation(geo);
                        tImageObject.setLabels(arrLabel);
                        lyrLocal.Shapes.add(tImageObject);
                        map.getOverlays().add(lyrLocal);
                        map.invalidate();*/
                        this.createNewTileLocalPosition(mq);

                    }else{
                        mapController = map.getController();

                        dataLabel = new TShapeLabel();
                        dataLabel.Text = "sdr";
                        dataLabel.AlignType = TLabelAlignType.RightCenter;
                        TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                        GeoPoint geoChild = new GeoPoint(mq.getLat(), mq.getLong());
                        mapController.animateTo(geoChild);
                        mapController.setCenter(geoChild);

                        tImageObject = new TShapeImage();
                        tImageObject.setLocation(geoChild);
                        tImageObject.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.drawable_position_green_small));
                        tImageObject.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baloontips_green_normal_left));
                        tImageObject.setLabels(arrLabel);
                        lyrLocal.Shapes.set(0, tImageObject);
                        //map.getOverlays().set(0, lyrLocal);
                        map.invalidate();
                    }
                }
            }// end of if local 000
            if (mq.getAddrId().equalsIgnoreCase("006")) { //delete free draw tactical
                Log.d(LOG_TAG, "getmessage del skenario :" + mq.getMessage());
                String skenarioMsg = mq.getMessage();

                String[] skenarioArrPortName = skenarioMsg.split(";",-1);

                Log.d(LOG_TAG, "getmessage del skenario :" + skenarioArrPortName[1]);
                if (skenarioArrPortName.length > 1) {
                    String[] skenarioArrParsed = skenarioArrPortName[1].split(",", -1);

                    Log.d(LOG_TAG, "getmessage idskenario :" + skenarioArrParsed[1]);


                    int indexer = Integer.parseInt(skenarioArrParsed[1]);
                    String skenarioSrcId = skenarioArrParsed[2];
                    Log.d(LOG_TAG, "getmessage skenarioSrcId :" + skenarioSrcId);
                    if (skenarioSrcId!=null){
                        String[] arrIdFromSkenarioSrcId = skenarioSrcId.split("~", -1);
                        int getIdFromSkenarioSrcId = Integer.parseInt(arrIdFromSkenarioSrcId[0]);
                        Log.d(TAG, "lyrTactical.Shapes:" + lyrTactical.Shapes.size());
                        for (int i = 0; i < lyrTactical.Shapes.size(); i++) {
                            Log.d(TAG, "getmessage getShapeName : " + lyrTactical.Shapes.get(i).getShapeName() + "==" + skenarioSrcId);
                            if (lyrTactical.Shapes.get(i).getShapeName().equalsIgnoreCase(skenarioSrcId)) {
                                lyrTactical.Shapes.remove(i);
                            }
                        }
                        dbManager.deleteSkenarioById(getIdFromSkenarioSrcId);
                        map.invalidate();
                    }
                }


            } //end of if local 006
            if (mq.getAddrId().equalsIgnoreCase("019")) { //sms
                //packetString driverview:local;019,0~38~0~1~5b922287~testsms~38
                /*
                Log.d(LOG_TAG, "getmessage sms :" + mq.getMessage());
                String smsOri = mq.getMessage();
                String[] arrPortName = smsOri.split(";",-1);
                String smsMsg =arrPortName[1];
                String[] arrParsed = smsMsg.split(",",-1);
                String[] arrMsg = arrParsed[1].split("~", -1);
                String msg = arrMsg[5];
                showToastMessage(msg); */
            }
        } //end of if local

        if (mq.getPortName().equalsIgnoreCase("child")){
            if (mq.getAddrId().equalsIgnoreCase("000")) {


                int countChild = lyrObjectSituation.Shapes.size();


                Log.d(TAG, "Lat Long Current Position child : " + "-Size:" + countChild + "-Lat:" + mq.getLat() + "," + mq.getLong());
                boolean statusOnlyChangeLoc = false;
                int iteration = 0;
                if (mq.getLat()!=0.0 && mq.getLong()!=0.0) {

                    for (int i = 0; i < countChild; i++) {
                        Log.i(TAG, "childId " + lyrObjectSituation.Shapes.get(i).getID() + " <> MQ Friend : " + mq.getFriendId() + "-name:" + mq.getMessage());
                        if (lyrObjectSituation.Shapes.get(i).getID() == mq.getFriendId()) {
                            statusOnlyChangeLoc = true;
                            Log.i(TAG, "only set location id :" + mq.getId() + " friendid:" + mq.getFriendId());

                            mapController = map.getController();

                            dataLabel = new TShapeLabel();
                            dataLabel.Text = "kwn " + mq.getFriendId();
                            dataLabel.AlignType = TLabelAlignType.LeftCenter;
                            TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                            GeoPoint geoChild = new GeoPoint(mq.getLat(), mq.getLong());
                            //mapController.animateTo(geoChild);
                            //mapController.setCenter(geoChild);

                            tImageObject = new TShapeImage();
                            tImageObject.setLocation(geoChild);
                            tImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                            tImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                            tImageObject.setLabels(arrLabel);
                            tImageObject.setID(mq.getFriendId());
                            tShapeImagesList.set(i, tImageObject);

                            lyrObjectSituation.Shapes.set(i, tShapeImagesList.get(i));
                            //map.getOverlays().set(0, lyrObjectSituation);
                            map.invalidate();

                            break;
                        }
                        iteration++;
                    }

                    if (statusOnlyChangeLoc!=true){
                        Log.i(TAG, "create new child with id : " + mq.getId() + "-Friend ID : " + mq.getFriendId());
                        this.deleteLyrKawan();

                        dataLabel = new TShapeLabel();
                        dataLabel.Text = "kwn " +  mq.getFriendId();
                        dataLabel.AlignType = TLabelAlignType.LeftCenter;
                        TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                        GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

                        tImageObject = new TShapeImage();
                        tImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                        tImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                        tImageObject.setLocation(geo);
                        tImageObject.setLabels(arrLabel);
                        tImageObject.setID(mq.getFriendId());
                        tShapeImagesList.add(tImageObject);

                        lyrObjectSituation.Shapes.add(tShapeImagesList.get(iteration));

                        map.getOverlays().add(lyrObjectSituation);
                        map.invalidate();
                    }
                } //end of ic get lat and long 0.0
            }
        }//end of if child

        if (mq.getPortName().equalsIgnoreCase("bcast")){
            if ((mq.getAddrId().equalsIgnoreCase("006")) || (mq.getAddrId().equalsIgnoreCase("000"))) {

                //int countBcast = lyrBcast.Shapes.size();
                int countBcast = lyrBcast.getShapes().size();
                //Log.d(TAG, "lyrBcast.getShapes().size():" + lyrBcast.getShapes().size());
                //Log.d(TAG, "lyrBcast.getShapes():" + lyrBcast.getShapes());

                Log.d(TAG, "Lat Long Current Position Bcast : " + "-Size:" + countBcast + "-Lat" + mq.getLat() + "," + mq.getLong());
                boolean statusOnlyChangeLoc = false;
                int iteration = 0;
                for(int i=0;i<countBcast;i++) {
                    Log.i(TAG,"bcast " + "name:" + lyrBcast.getShapes().get(i).getShapeName() + "-id:" + lyrBcast.getShapes().get(i).getID() + " <> MQ Friend : " + mq.getFriendId() + "-name:" + mq.getMessage());
                    //lyrBcast.Shapes.get(i).getID()
                        if (lyrBcast.getShapes().get(i).getID()==0){
                            tBcastImageObject.setID(mq.getFriendId());
                        }
                        if (lyrBcast.getShapes().get(i).getID() == mq.getFriendId()) {
                            statusOnlyChangeLoc = true;
                            Log.i(TAG, "only set location bcast id :" + mq.getId() + " friendid:" + mq.getFriendId());

                            mapController = map.getController();

                            dataLabel = new TShapeLabel();
                            dataLabel.Text = "kwn " + mq.getFriendId();
                            //dataLabel.Text = "bcast";
                            dataLabel.AlignType = TLabelAlignType.LeftCenter;
                            TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                            GeoPoint geoBcast = new GeoPoint(mq.getLat(), mq.getLong());
                            //mapController.animateTo(geoBcast);
                            //mapController.setCenter(geoBcast);

                            tBcastImageObject = new TShapeImage();
                            tBcastImageObject.setLocation(geoBcast);
                            tBcastImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                            tBcastImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                            tBcastImageObject.setLabels(arrLabel);
                            tBcastImageObject.setID(mq.getFriendId());
                            tBcastImageObject.setShapeName("bcast");
                            tBcastShapeImagesList.set(i, tBcastImageObject);

                            lyrBcast.Shapes.set(i, tBcastShapeImagesList.get(i));
                            //map.getOverlays().set(0, lyrBcast);
                            map.invalidate();

                            break;
                        }
                    iteration++;
                }

                if (statusOnlyChangeLoc!=true){
                    Log.i(TAG, "create new bcast with id : " + mq.getId() + "-Friend ID : " + mq.getFriendId());
                    deleteLyrKawan();
                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "kwn " + mq.getFriendId();
                    //dataLabel.Text = "bcast";
                    dataLabel.AlignType = TLabelAlignType.LeftCenter;
                    TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                    GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

                    tBcastImageObject = new TShapeImage();
                    tBcastImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                    tBcastImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                    tBcastImageObject.setLocation(geo);
                    tBcastImageObject.setLabels(arrLabel);
                    tBcastImageObject.setID(mq.getFriendId());
                    tBcastImageObject.setShapeName("bcast");
                    tBcastShapeImagesList.add(tBcastImageObject);

                    lyrBcast.Shapes.add(tBcastShapeImagesList.get(iteration));

                    map.getOverlays().add(lyrBcast);
                    map.invalidate();
                    //this.createNewTileFriendPosition(mq, iteration, lyrBcast);
                }
            }
            if (mq.getAddrId().equalsIgnoreCase("004")) {
                String skenarioMsg = mq.getMessage();
                //skenarioMsg = "bcast;004,65535,39,3,Ãabcdef,1,desc,4,-6.91484,107.362352,-6.915238,107.468248,-6.87643,107.464528,-6.880324,107.460184";
                //skenarioMsg = "bcast;004,65535,38,1,Ãtactical,1,,4,-6.759846,107.5016,-6.891448,107.267496,-6.896875,107.509808,-7.010841,107.394816"
                Log.d(LOG_TAG, "getmessage skenario :" + skenarioMsg);
                String[] skenarioArrPortName = skenarioMsg.split(";",-1);

                Log.d(LOG_TAG, "getmessage skenario :" + skenarioArrPortName[1]);
                if (skenarioArrPortName.length > 1) {
                    String[] skenarioArrParsed = skenarioArrPortName[1].split(",", -1);
                    Log.d(LOG_TAG, "getmessage skenario :" + skenarioArrParsed[1]);

                    ArrayList<GeoPoint> geoPoints = new ArrayList<>();
                    ArrayList<Double>  pointsDouble = new ArrayList<>();
                    GeoPoint point;
                    if (skenarioArrParsed.length > 0) {
                        for (int i =8; i < skenarioArrParsed.length; i+=2) {
                            Log.d(LOG_TAG, "getmessage skenario lat:" + i + ":" + skenarioArrParsed[i]);
                            Log.d(LOG_TAG, "getmessage skenario long:" + i + "+1:" + skenarioArrParsed[i + 1]);

                            pointsDouble.add(Double.parseDouble(skenarioArrParsed[i]));
                            pointsDouble.add(Double.parseDouble(skenarioArrParsed[i + 1]));

                            point = new GeoPoint(Double.parseDouble(skenarioArrParsed[i]), Double.parseDouble(skenarioArrParsed[i + 1]));
                            geoPoints.add(point);
                        }
                    }

                    String skenarioSrcId = dbManager.insertSkenarioPoints(skenarioArrParsed,pointsDouble);

                    TShapePolyline tShapePolyline = new TShapePolyline();
                    tShapePolyline.setColor(Color.RED);
                    tShapePolyline.setShapeName(skenarioSrcId);
                    tShapePolyline.setIsRute(true);
                    tShapePolyline.setLocations(geoPoints);
                    int indexer = Integer.parseInt(skenarioArrParsed[3]);
                    tShapePolyline.setID(indexer);
                    //tShapePolyline.setDashed(true);

                    lyrTactical.Shapes.add(tShapePolyline);
                    map.getOverlays().add(lyrTactical);
                    map.invalidate();

                }
            }
        }//end of if bcast


        if (mq.getPortName().equalsIgnoreCase("neigh")){
            if (mq.getAddrId().equalsIgnoreCase("000")) {

                //int countneighbour = lyrNeighbour.Shapes.size();
                int countneighbour = lyrNeighbour.getShapes().size();

                Log.d(TAG, "Lat Long Current Position neighbour : " + "-Size:" + countneighbour + "-Lat" + mq.getLat() + "," + mq.getLong());
                boolean statusOnlyChangeLoc = false;
                int iteration = 0;
                for(int i=0;i<countneighbour;i++) {

                    Log.i(TAG,"neighbour " + "name:" + lyrNeighbour.getShapes().get(i).getShapeName()+ "-id:" + lyrNeighbour.getShapes().get(i).getID() + " <> MQ Friend : " + mq.getFriendId() + "-name:" + mq.getMessage());

                        if (lyrNeighbour.getShapes().get(i).getID() == mq.getFriendId()) {
                            statusOnlyChangeLoc = true;
                            Log.i(TAG, "only set location neighbour id :" + mq.getId() + " friendid:" + mq.getFriendId());

                            mapController = map.getController();

                            dataLabel = new TShapeLabel();
                            dataLabel.Text = "kwn " + mq.getFriendId();
                            dataLabel.AlignType = TLabelAlignType.LeftCenter;
                            TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                            GeoPoint geoBcast = new GeoPoint(mq.getLat(), mq.getLong());
                            //mapController.animateTo(geoBcast);
                            //mapController.setCenter(geoBcast);

                            tNeighbourImageObject = new TShapeImage();
                            tNeighbourImageObject.setLocation(geoBcast);
                            tNeighbourImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                            tNeighbourImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                            tNeighbourImageObject.setLabels(arrLabel);
                            tNeighbourImageObject.setID(mq.getFriendId());
                            tNeighbourImageObject.setShapeName("neighbour");
                            tNeighbourShapeImagesList.set(i, tNeighbourImageObject);

                            lyrNeighbour.Shapes.set(i, tNeighbourShapeImagesList.get(i));
                            //map.getOverlays().set(0, lyrNeighbour);
                            map.invalidate();

                            break;
                        }

                    iteration++;
                }

                if (statusOnlyChangeLoc!=true){
                    Log.i(TAG, "create new neighbour with id : " + mq.getId() + "-Friend ID : " + mq.getFriendId());

                    deleteLyrKawan();

                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "kwn " + mq.getFriendId();
                    dataLabel.AlignType = TLabelAlignType.LeftCenter;
                    TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                    GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

                    tNeighbourImageObject = new TShapeImage();
                    tNeighbourImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
                    tNeighbourImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
                    tNeighbourImageObject.setLocation(geo);
                    tNeighbourImageObject.setLabels(arrLabel);
                    tNeighbourImageObject.setID(mq.getFriendId());
                    tNeighbourImageObject.setShapeName("neighbour");
                    tNeighbourShapeImagesList.add(tNeighbourImageObject);

                    lyrNeighbour.Shapes.add(tNeighbourShapeImagesList.get(iteration));

                    map.getOverlays().add(lyrNeighbour);
                    map.invalidate();
                    //this.createNewTileFriendPosition(mq, iteration, lyrNeighbour);
                }
            }
        }//end of if neighbour

        if (mq.getPortName().equalsIgnoreCase("trap")){
            if (mq.getAddrId().equalsIgnoreCase("003")) {
                //packetString driverview:trap;003,38,-6.842071,107.409352,18101,38,1,musuh,1,,38
                String trapMsg = mq.getMessage();
                Log.d(LOG_TAG, "getmessage trap :" + trapMsg);
                String[] trapArrPortName = trapMsg.split(";",-1);

                Log.d(LOG_TAG, "getmessage trap :" + trapArrPortName[1]);
                if (trapArrPortName.length > 1) {
                    String[] trapArrParsed = trapArrPortName[1].split(",", -1);
                    String idImage = trapArrParsed[4];
                    Log.d(LOG_TAG, "getmessage trap :" + trapArrParsed[1] + "-idImage" + idImage);
                    if (dbManager.getTacticalSymbol(idImage)!=null){
                        //ivTacticalSymbol.setImageBitmap(dbManager.getTacticalSymbol(idImage));
                        TShapeImage tImageObject = new TShapeImage();
                        tImageObject.setImage(dbManager.getTacticalSymbol(idImage));
                        tImageObject.setLocation(new GeoPoint(mq.getLat(), mq.getLong()));
                        //tImageObject.setLabels(dta);
                        lyrLocal.getShapes().add(tImageObject);
                        map.getOverlays().add(lyrLocal);
                        map.invalidate();
                    }
                }
            }//end of 003 trap
        } //end of trap

    }


    @Subscribe
    public void onSendMessage(String strMessage)
    {
        String[] arrMessage=strMessage.split(";", -1);
        if(arrMessage.length > 0){
            Log.d(LOG_TAG, "getmessage sms :" + strMessage);

            String smsMsg =arrMessage[1];
            String[] arrParsed = smsMsg.split(",",-1);
            String[] arrMsg = arrParsed[1].split("~", -1);
            String msg = arrMsg[5];
            //showToastMessage(msg);
            smsFragment.setValue(msg);
            smsFragment.show(fragmentManager, "SMS");
        }

        Log.d(TAG, "onSendMessage:" + strMessage);
        if (strMessage.equalsIgnoreCase("true")){
            animation.cancel();
            ic_disconnect.setVisibility(View.GONE);
        }else{
            ic_disconnect.setVisibility(View.VISIBLE);
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            ic_disconnect.startAnimation(animation);
        }
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            //mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            //mTextView.setTextColor(getResources().getColor(android.R.color.white));
            //mClockView.setVisibility(View.VISIBLE);

            //mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            //mContainerView.setBackground(null);
            //mTextView.setTextColor(getResources().getColor(android.R.color.black));
            //mClockView.setVisibility(View.GONE);
        }
    }

    public void createNewTileLocalPosition(ModelMessage mq){
        dataLabel = new TShapeLabel();
        dataLabel.Text = "sdr";
        dataLabel.AlignType = TLabelAlignType.RightCenter;
        TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

        GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

        tImageObject = new TShapeImage();
        tImageObject.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.drawable_position_green_small));
        tImageObject.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baloontips_green_normal_left));
        tImageObject.setLocation(geo);
        tImageObject.setLabels(arrLabel);
        lyrLocal.Shapes.add(tImageObject);
        map.getOverlays().add(lyrLocal);
        map.invalidate();
    }

    public void createNewTileFriendPosition(ModelMessage mq, int iteration, TShapeOverlay lyr){

        dataLabel = new TShapeLabel();
        //dataLabel.Text = "kwn " +  mq.getFriendId();
        dataLabel.Text = "kwn";
        dataLabel.AlignType = TLabelAlignType.LeftCenter;
        TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

        GeoPoint geo = new GeoPoint(mq.getLat(), mq.getLong());

        tImageObject = new TShapeImage();
        tImageObject.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.drawable_position_blue_small));
        tImageObject.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baloontips_blue_normal));
        tImageObject.setLocation(geo);
        tImageObject.setLabels(arrLabel);
        tImageObject.setID(mq.getFriendId());
        tShapeImagesList.add(tImageObject);

        lyr.Shapes.add(tShapeImagesList.get(iteration));

        map.getOverlays().add(lyr);
        map.invalidate();
    }

    public void deleteLyrKawan(){
        int sizeLyrKawan = lyrKawan.getShapes().size();
        Log.d(TAG, "delete layer kawan:" + sizeLyrKawan);
        lyrKawan.getShapes().clear();
//        for (int i=0;i<sizeLyrKawan;i++){
//            lyrKawan.Shapes.remove(i);
//        }
        map.invalidate();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        Log.d("tap", "singleTapConfirmedHelper");
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        Log.d("tap", "longPressHelperlongPressHelper");
        return false;
    }
}
