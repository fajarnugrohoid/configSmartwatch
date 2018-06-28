package com.bms.user.bmssmartwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import org.osmdroid.api.IGeoPoint;
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
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;

import message.EventMessage;
import message.FirerequestSyncData;
import services.ChildService;
import services.LocalService;
import services.NeighbourService;
import shape.ShapeIcon;
import shape.ShapeOverlay;
import shape.TLabelAlignType;
import shape.TShapeImage;
import shape.TShapeLabel;
import shape.TShapeOverlay;
import model.ModelMessage;
import services.BroadcastService;

public class MainActivity extends WearableActivity implements MapListener,MapEventsReceiver{
//public class MainActivity extends WearableActivity implements MapListener{
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    FolderOverlay activeLatLonGrid;

    public Context context;

    MapView map = null;
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
    FirerequestSyncData fireRequestMessageGetReqSync;
    FirerequestSyncData fireRequestMessageSetReqSync;

    Drawable markerTP, myMarker;
    IMapController mapController;
    OverlayItem myCurrentPosition;
    ShapeOverlay lyr, lyrChild;
    TShapeOverlay lyrObjectSituation, lyrLocal, lyrBcast, lyrNeighbour;
    Location loc,locChild;
    Bitmap bmp, bmpChild, bmpChild2;
    TShapeImage shpImage;
    ShapeIcon icon, iconChild;
    List<ShapeIcon> shapeIconsList = new ArrayList<ShapeIcon>();
    List<Location> locList = new ArrayList<Location>();

    List<TShapeImage> tShapeImagesList = new ArrayList<TShapeImage>();
    List<TShapeImage> tBcastShapeImagesList = new ArrayList<TShapeImage>();
    List<TShapeImage> tNeighbourShapeImagesList = new ArrayList<TShapeImage>();

    TShapeLabel dataLabel;
    TShapeImage tImageObject,tBcastImageObject, tNeighbourImageObject;

    final Context c = this;

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

        map.setUseDataConnection(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setMapListener(this);
        map.setMinZoomLevel(9);
        map.setMaxZoomLevel(23);

        String imgPath = android.os.Environment.getExternalStorageDirectory().toString()+"/Download/Cipatat/Cipatat/Topografi";
        AssetsTileSource source = new AssetsTileSource(null,imgPath,11,14,256,".png");
        MapTileModuleProviderBase moduleProvider = new MapTileFileAssetsProvider(source);
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(getApplicationContext());
        MapTileProviderArray tileProviderArray = new MapTileProviderArray(source,simpleReceiver, new MapTileModuleProviderBase[]{moduleProvider});
        map.setTileProvider(tileProviderArray);

        mapController = map.getController();
        mapController.setZoom(12);
        GeoPoint point = new GeoPoint(-6.823108, 107.388532);
        mapController.setCenter(point);

        lyrLocal = new TShapeOverlay(this ,"tshapeoverlaymain");
        lyrBcast = new TShapeOverlay(this, "tshapeoverlaybcast");
        lyrNeighbour = new TShapeOverlay(this, "tshapeoverlayneighbour");
        lyrObjectSituation = new TShapeOverlay(this, "tshapeoverlaychild");

        /*
        ModelMessage mm;
        ArrayList<ModelMessage> arrMm = new ArrayList<ModelMessage>();
        mm = new ModelMessage(0,"child", "0", 0, -6.819604, 107.399176, "test0");arrMm.add(mm);
        arrMm.add(new ModelMessage(1,"child", "1", 1, -6.830662, 107.373180, "test1"));
        arrMm.add(new ModelMessage(2,"child", "2", 2, -6.860403, 107.389316, "test2"));


        for(int i=0;i<arrMm.size();i++){
            dataLabel = new TShapeLabel();
            dataLabel.Text = arrMm.get(i).getMessage();
            dataLabel.AlignType = TLabelAlignType.LeftCenter;
            TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

            tImageObject = new TShapeImage();
            tImageObject.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.ic_button_location));
            tImageObject.setLocation(new GeoPoint(arrMm.get(i).getLat(), arrMm.get(i).getLong()));
            tImageObject.setLabels(arrLabel);
            tImageObject.setID(arrMm.get(i).getId());
            tShapeImagesList.add(tImageObject);
        }

        for(int i=0;i<tShapeImagesList.size();i++){
            lyrObjectSituation.Shapes.add(tShapeImagesList.get(i));
        } */

        //MapEventsOverlay OverlayEventos = new MapEventsOverlay(MainActivity.getBaseContext(), mReceive);
        //mMapView.getOverlays().add(OverlayEventos);

        map.setMapListener(this);



        map.getOverlays().add(lyrLocal);
        map.getOverlays().add(lyrBcast);
        map.getOverlays().add(lyrNeighbour);
        map.getOverlays().add(lyrObjectSituation);

        map.invalidate();

//        for(int i=0;i<lyrObjectSituation.Shapes.size();i++) {
//            Log.i(TAG, "shapeId:" + lyrObjectSituation.Shapes.get(i).getID() + "-idArrMm:" + arrMm.get(i).getId() + "-name:" + arrMm.get(i).getMessage());
//        }



        Intent intent = new Intent(this, LocalService.class);
        startService(intent);


        Intent childService = new Intent(this, ChildService.class);
        startService(childService);

        Intent neighbourService = new Intent(this, NeighbourService.class);
        startService(neighbourService);

        Intent broadcastService = new Intent(this, BroadcastService.class);
        startService(broadcastService);
        //mReceive;
    }


    void showToastMessage(String message) {
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


    /*
    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Log.i(TAG, "Long press!");
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);

            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here
                        }
                    })

                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();

        }
    }; */

    /*
    @Override
    public boolean onLongPress(MotionEvent event, TileView mapView) {
        final int index = getMarkerAtPoint((int)event.getX(), (int)event.getY(), mapView);
        mapView.mPoiMenuInfo.MarkerIndex = index;
        mapView.mPoiMenuInfo.EventGeoPoint = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY(), mapView.getBearing());
        if (index >= 0)
            if (onLongLongPress(index))
                return true;

        return super.onLongPress(event, mapView);
    } */

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            handler.postDelayed(mLongPressed, 1000);
        if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() ==     MotionEvent.ACTION_UP))
            handler.removeCallbacks(mLongPressed);
        return false;
    } */

    /*
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e != null) {
            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Press Action Down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "Press Action MOve");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "Press Action Up");
                    break;
            }
            return true;
        } else {
            return false;
        }
    } */



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
            int count = lyrLocal.Shapes.size();
            Log.i(TAG, "Lat Long Current Position Local : " + mq.getLat() + "," + mq.getLong() + "-count:" + count);
            if (mq.getLat()!=0.0 && mq.getLong()!=0.0){
                if (count <= 0){
                    //create new
                    Log.i(TAG, "create new local count:" + count);
                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "local";
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
                    map.invalidate();

                }else{
                    mapController = map.getController();

                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "local";
                    dataLabel.AlignType = TLabelAlignType.RightCenter;
                    TShapeLabel[] arrLabel = new TShapeLabel[]{dataLabel};

                    GeoPoint geoChild = new GeoPoint(mq.getLat(), mq.getLong());
                    mapController.animateTo(geoChild);
                    mapController.setCenter(geoChild);

                    tImageObject = new TShapeImage();
                    tImageObject.setLocation(geoChild);
                    tImageObject.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.drawable_position_blue_small));
                    tImageObject.setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baloontips_blue_normal_right));
                    tImageObject.setLabels(arrLabel);
                    lyrLocal.Shapes.set(0, tImageObject);
                    //map.getOverlays().set(0, lyrLocal);
                    map.invalidate();
                }
            }
        } //end of if local

        if (mq.getPortName().equalsIgnoreCase("child")){
            if (mq.getAddrId().equalsIgnoreCase("000")) {

                int countChild = lyrObjectSituation.Shapes.size();

                Log.d(TAG, "Lat Long Current Position Child : " + "-Size:" + countChild + "-Lat:" + mq.getLat() + "," + mq.getLong());
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
                            dataLabel.Text = "child " + mq.getFriendId();
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
                        Log.i(TAG, "create new with id : " + mq.getId() + "-Friend ID : " + mq.getFriendId());

                        dataLabel = new TShapeLabel();
                        dataLabel.Text = "child " +  mq.getFriendId();
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
            if (mq.getAddrId().equalsIgnoreCase("000")) {

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

                        if (lyrBcast.getShapes().get(i).getID() == mq.getFriendId()) {
                            statusOnlyChangeLoc = true;
                            Log.i(TAG, "only set location bcast id :" + mq.getId() + " friendid:" + mq.getFriendId());

                            mapController = map.getController();

                            dataLabel = new TShapeLabel();
                            dataLabel.Text = "" + mq.getFriendId();
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

                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "" +  mq.getFriendId();
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
                }
            }
        }//end of if bcast

        if (mq.getPortName().equalsIgnoreCase("neighbour")){
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
                            dataLabel.Text = "" + mq.getFriendId();
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

                    dataLabel = new TShapeLabel();
                    dataLabel.Text = "" + mq.getFriendId();
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
                }
            }
        }//end of if neighbour

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
