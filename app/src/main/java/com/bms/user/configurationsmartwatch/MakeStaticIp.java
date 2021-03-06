package com.bms.user.configurationsmartwatch;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;

import static com.bms.user.configurationsmartwatch.MainActivity.TAG;

public class MakeStaticIp {

    public void MakeStaticIp(Context context, WifiConfiguration wifiConf)
    {
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "MakeStaticIp.wifiConf:" + wifiConf);
        if (wifiConf != null)
        {
            try
            {

                Log.d(TAG, "MakeStaticIp.setStaticIpConfiguration:");

                setStaticIpConfiguration(manager, wifiConf,
                        InetAddress.getByName("192.168.15.166"), 24,
                        InetAddress.getByName("192.168.15.13"),
                        new InetAddress[] { InetAddress.getByName("203.142.82.222"), InetAddress.getByName("8.8.8.8") });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void setStaticIpConfiguration(WifiManager manager, WifiConfiguration config, InetAddress ipAddress, int prefixLength, InetAddress gateway, InetAddress[] dns) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException
    {
        // First set up IpAssignment to STATIC.
        Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
        Log.d(TAG, "Object.ipAssignment:" + ipAssignment);
        callMethod(config, "setIpAssignment", new String[] { "android.net.IpConfiguration$IpAssignment" }, new Object[] { ipAssignment });

        // Then set properties in StaticIpConfiguration.
        Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");
        Log.d(TAG, "setStaticIpConfiguration.staticIpConfig:" + staticIpConfig);
        Object linkAddress = newInstance("android.net.LinkAddress", new Class<?>[] { InetAddress.class, int.class }, new Object[] { ipAddress, prefixLength });
        Log.d(TAG, "setStaticIpConfiguration.linkAddress:" + linkAddress);
        setField(staticIpConfig, "ipAddress", linkAddress);
        setField(staticIpConfig, "gateway", gateway);
        getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
        for (int i = 0; i < dns.length; i++) {
            getField(staticIpConfig, "dnsServers", ArrayList.class).add(dns[i]);
            Log.d(TAG, "setStaticIpConfiguration.dns:" + dns[i]);
        }

        callMethod(config, "setStaticIpConfiguration", new String[] { "android.net.StaticIpConfiguration" }, new Object[] { staticIpConfig });
        manager.updateNetwork(config);
        manager.saveConfiguration();
    }


    private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        return newInstance(className, new Class<?>[0], new Object[0]);
    }

    private static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
    {
        Class<?> clz = Class.forName(className);
        Constructor<?> constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException
    {
        Class<Enum> enumClz = (Class<Enum>)Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    private static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
    {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    private static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
    {
        Field field = object.getClass().getDeclaredField(fieldName);
        return type.cast(field.get(object));
    }

    private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
    {
        Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++){
            parameterClasses[i] = Class.forName(parameterTypes[i]);
            Log.d(TAG, "Object.ipAssignment.callMethod:" + parameterClasses[i]);
        }

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        Log.d(TAG, "Object.ipAssignment.method:" + method);
        method.invoke(object, parameterValues);
        Log.d(TAG, "Object.ipAssignment.parameterValues:" + parameterValues);
    }
}
