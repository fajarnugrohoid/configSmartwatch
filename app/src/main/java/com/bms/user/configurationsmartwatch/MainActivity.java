package com.bms.user.configurationsmartwatch;

import android.app.Activity;
import android.app.ActivityManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.RotaryEncoder;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
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
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.bms.user.database.DbManager;
import com.bms.user.listview.ListViewAdapter;
import com.bms.user.listview.ListViewItem;
import com.bms.user.model.ModelSetIp;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import org.osmdroid.views.overlay.FolderOverlay;

public class MainActivity extends WearableActivity implements WearableListView.ClickListener{
//public class MainActivity extends WearableActivity implements MapListener{
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);
    public static final String TAG = "ConfigurationSmartwatch";

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    FolderOverlay activeLatLonGrid;

    public Context context;

    private List<ListViewItem> viewItemList = new ArrayList<>();
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        //mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        //mTextView = (TextView) findViewById(R.id.text);
        //mClockView = (TextView) findViewById(R.id.text);


        Context ctx = getApplicationContext();
        isStoragePermissionGranted();
        isChangeWifiStatePermissionGranted();
        isAccessNetworkStateGranted();
        isAccessWifiStateGranted();
        dbManager = new DbManager(ctx);
        //SET_WIFI();
        //ConfigurationNetwork configurationNetwork = new ConfigurationNetwork();
        //configurationNetwork.ConfigNetworkIP(ctx);


        WearableListView wearableListView = (WearableListView) findViewById(R.id.wearable_list_view);

        viewItemList.add(new ListViewItem(R.drawable.ic_gear, "Choose Smartwatch As"));
        viewItemList.add(new ListViewItem(R.drawable.ic_map, "Choose Map"));
        viewItemList.add(new ListViewItem(R.drawable.ic_running, "Group"));
        viewItemList.add(new ListViewItem(R.drawable.ic_running, "Clear All"));
        viewItemList.add(new ListViewItem(R.drawable.ic_skateboard, "Set Ip"));
        viewItemList.add(new ListViewItem(R.drawable.ic_skateboard, "Connect Wifi"));

        wearableListView.setAdapter(new ListViewAdapter(this, viewItemList));
        wearableListView.setClickListener(this);

        //Wearable.WearableOptions options = new Wearable.WearableOptions.Builder().setLooper(myLooper).build();
        DataClient mDataClient = Wearable.getDataClient(this);
        Log.d(TAG, "mDataClient.getDataItems:" + mDataClient.getDataItems());


        mDataClient.addListener(new DataClient.OnDataChangedListener() {
            @Override
            public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
                     Log.d(TAG, "dataEventBuffer:" + dataEventBuffer.getStatus());
            }
        });


    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_SCROLL && RotaryEncoder.isFromRotaryEncoder(ev)) {
            // Note that we negate the delta value here in order to get the right scroll direction.
            float delta = -RotaryEncoder.getRotaryAxisValue(ev)
                    * RotaryEncoder.getScaledScrollFactor(getApplicationContext());
            //scrollBy(0, Math.round(delta));
            Log.d(TAG, "rotary input up delta:" + delta + "-math round:" + Math.round(delta));

            return true;
        }
        return super.onGenericMotionEvent(ev);
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
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Change Wifi State Permission is granted");
            return true;
        }
    }

    public  boolean isAccessNetworkStateGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Access Network State Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Access Network State Permission is revoked");
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Access Network State Permission is granted");
            return true;
        }
    }

    public  boolean isAccessWifiStateGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Access Wifi State Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Access Wifi State Permission is revoked");
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Access Wifi State Permission is granted");
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
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Fine Location Permission is granted");
            return true;
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

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        //Toast.makeText(this, "Click on " + viewItemList.get(viewHolder.getLayoutPosition()).text, Toast.LENGTH_SHORT).show();
        //int chooseIndex = viewItemList.get(viewHolder.getLayoutPosition());
        int idx=viewHolder.getLayoutPosition();
        Log.d(TAG, "viewHolder index:" + idx);
        if (idx==0){
            Intent appInfo = new Intent(this, SendToActivity.class);
            startActivity(appInfo);
        }else if (idx==1){
            Intent appInfo = new Intent(this, ChooseMapsActivity.class);
            startActivity(appInfo);
        }else if (idx==2){
            Intent appInfo = new Intent(this, SetGroupActivity.class);
            startActivity(appInfo);
        }else if (idx==3){
            int statusClearAll = dbManager.clearAll();
            ActivityManager am = (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.bms.user.bmssmartwatch");
            if (statusClearAll!=0){
                Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
            }
        }else if (idx==4){
            Intent appInfo = new Intent(this, SetIpActivity.class);
            startActivity(appInfo);
        }else if (idx==5){
            //connect
            ModelSetIp modelSetIp = new ModelSetIp();
            modelSetIp = dbManager.getWifiConfig();
            SET_WIFI(modelSetIp);
        }



    }


    private void SET_WIFI(ModelSetIp modelSetIp) {
        // TODO Auto-generated method stub

        WifiConfiguration wifiConfig = new WifiConfiguration();
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiConfig.SSID = String.format("\"%s\"", modelSetIp.getSSIDName().toString());
        wifiConfig.preSharedKey = String.format("\"%s\"", modelSetIp.getSSIDPassword().toString());


        try {
            setStaticIpConfiguration(wifiManager, wifiConfig,
                    InetAddress.getByName(modelSetIp.getMyIp()),
                    Integer.valueOf(24),
                    InetAddress.getByName(modelSetIp.getMyGateway().toString()),
                    new InetAddress[]{InetAddress.getByName("8.8.8.8"), InetAddress.getByName("8.8.4.4")});

        } catch (Exception e) {
            e.printStackTrace();
        }

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    @SuppressWarnings("unchecked")
    public static void setStaticIpConfiguration(WifiManager manager, WifiConfiguration config, InetAddress ipAddress, int prefixLength, InetAddress gateway, InetAddress[] dns) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
        // First set up IpAssignment to STATIC.
        Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
        callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"}, new Object[]{ipAssignment});

        // Then set properties in StaticIpConfiguration.
        Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");
        Object linkAddress = newInstance("android.net.LinkAddress", new Class<?>[]{InetAddress.class, int.class}, new Object[]{ipAddress, prefixLength});

        setField(staticIpConfig, "ipAddress", linkAddress);
        setField(staticIpConfig, "gateway", gateway);
        getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
        for (int i = 0; i < dns.length; i++)
            getField(staticIpConfig, "dnsServers", ArrayList.class).add(dns[i]);

        callMethod(config, "setStaticIpConfiguration", new String[]{"android.net.StaticIpConfiguration"}, new Object[]{staticIpConfig});

        int netId = manager.updateNetwork(config);
        boolean result = netId != -1;
        if (result) {
            //boolean isDisconnected = manager.disconnect();
            boolean configSaved = manager.saveConfiguration();
            boolean isEnabled = manager.enableNetwork(config.networkId, true);
            boolean isReconnected = manager.reconnect();
        }
    }
    public static WifiConfiguration getCurrentWiFiConfiguration(Context context) {
        WifiConfiguration wifiConf = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                if (configuredNetworks != null) {
                    for (WifiConfiguration conf : configuredNetworks) {
                        if (conf.networkId == connectionInfo.getNetworkId()) {
                            wifiConf = conf;
                            break;
                        }
                    }
                }
            }
        }
        return wifiConf;
    }

    private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        return newInstance(className, new Class<?>[0], new Object[0]);
    }

    private static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        Constructor<?> constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException {
        Class<Enum> enumClz = (Class<Enum>) Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    private static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    private static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return type.cast(field.get(object));
    }

    private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            parameterClasses[i] = Class.forName(parameterTypes[i]);

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        method.invoke(object, parameterValues);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
