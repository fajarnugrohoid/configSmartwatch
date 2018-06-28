package services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import message.TCFMessage;
import message.TCFMessageConverter;
import message.TCFMessageParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by user on 19/04/2018.
 */

public class SocketServices extends Service {
    public static AtomicBoolean IsInterrupted = new AtomicBoolean();
    public  static ConcurrentLinkedQueue<TCFMessage> OutQueue = new ConcurrentLinkedQueue<>();
    public  static ConcurrentLinkedQueue<TCFMessage> InQueue = new ConcurrentLinkedQueue<>();
    public  static AtomicBoolean IsConnected = new AtomicBoolean();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String ipAddress="10.0.2.2";
    private static String port = "9000";
    public static Socket tcpSocket;


    private static Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DataLinkService","On Created Service");
        Log.i("RepoService","ipAddress "+ipAddress+" port "+port);

    }

    public static void Connect(String ip, String ports) {
        ipAddress = ip;
        port = ports;

        IsInterrupted.set(false);
        IsConnected.set(false);

        handler.postDelayed(tickRunnable,100);

        new Thread(runnable).start();
        //startForeground(0, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // start new thread and you your work there

        IsInterrupted.set(false);
        IsConnected.set(false);

        handler.postDelayed(tickRunnable,100);

        new Thread(runnable).start();
        startForeground(0, null);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //eventBus.unregister(this);
    }


    private static Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            //read incoming queue
            while (!InQueue.isEmpty())
            {
                TCFMessage msg = InQueue.poll();

                switch (msg.getMessageType())
                {
                    case ContollerStatus: {
                        Log.i("RepoService","contrller");
                        break;
                    }
                    case ShooterStatus: {

                        break;
                    }
                    case ObserverStatus:{

                        break;
                    }
                    case FireRequest:{

                        break;
                    }

                    case FireReply:{

                        break;
                    }

                    case FireOrder:{

                        break;
                    }

                    case TacticalObject:{

                        break;
                    }

                    case ImageData: {

                        break;
                    }

                    case WeatherStatus:
                    {

                        break;
                    }
                    case TextMessage:
                    {

                        break;
                    }
                    case Time:{

                        break;

                    }
                    case Battery:{

                        break;
                    }
                    case CheckCCUDevice :{

                        break;

                    }
                    case SyncData:{
                        Log.i("RepoService","Receive ReqSync");
                        /*
                        FirerequestSyncData res = (FirerequestSyncData) msg;
                        ((SyncApp)getApplication()).getRepository().setFirerequestSyncData(res);
                        if(res.Command.equals(FireRequestCommand.Send)){
                            eventBus.post(GeneralEvent.RequestSyncFireRequest);
                        }else{
                            eventBus.post(GeneralEvent.ReplySyncFireRequest);
                        } */
                        break;
                    }
                }
            }

            handler.postDelayed(this,100);
        }
    };

    private static Runnable runnable = new Runnable() {

        boolean isConnected = false;
        //        Socket tcpSocket;
        TCFMessageParser parser =new TCFMessageParser(InQueue);
        @Override
        public void run() {
            tcpSocket = new Socket();
            try{
                //tcpSocket.setKeepAlive(true);
                tcpSocket.setKeepAlive(false);
            }
            catch (SocketException e)
            {

            }

            while(!IsInterrupted.get()) {
//                Log.i("service","while !IsInterrupted  || tcpsocket connect "+tcpSocket.isConnected()+" tcpsocket close? "+tcpSocket.isClosed());

                if(tcpSocket!=null)
                    if (!tcpSocket.isConnected() || tcpSocket.isClosed())
                    {
                        try{
                            tcpSocket.setSoTimeout(6000);
                            tcpSocket.connect(new InetSocketAddress(ipAddress, Integer.valueOf(port)),1000);

                            if (tcpSocket.isConnected())
                            {
                                Intent intent = new Intent("tcf.linkStatus");
                                if(tcpSocket!=null)
                                    intent.putExtra("status",tcpSocket.isConnected());
                                //sendBroadcast(intent);
                                IsConnected.set(true);
                                System.out.println("connected");
                            }

                        }
                        catch (IOException e)
                        {
                            Intent intent = new Intent("tcf.linkStatus");
                            if(tcpSocket!=null){
                                intent.putExtra("status",tcpSocket.isConnected());
                                //sendBroadcast(intent);
                                IsConnected.set(false);
                            }
                            System.out.println("not connect");
                            try {
                                if(tcpSocket!=null)
                                    tcpSocket.close();
                                tcpSocket = null;
                                tcpSocket = new Socket();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            Log.i("service","not connected interrupted"+IsInterrupted);
                            Log.i("service","not connected tcpsocket "+tcpSocket);
                            try {
                                if(tcpSocket!=null)
                                    tcpSocket.close();
                                tcpSocket = null;
                                tcpSocket = new Socket();
                            }
                            catch (IOException e1)
                            {

                            }
                        }
                    }
                    else
                    {
                        //send message
//
                        while(!OutQueue.isEmpty())
                        {
                            TCFMessage s = OutQueue.poll();
                            //logger.error(s);\
                            byte[] buffer = TCFMessageConverter.ConvertMessage(s);
                            if (buffer == null) continue;
                            try
                            {

                                System.out.println("buffer.length : "+buffer.length);
                                byte[] buffer2 = new byte[buffer.length + 2];
                                buffer2[0] = 0x02;
                                System.out.println("buffer.length [0]: "+buffer2[0]);
                                System.out.println("buffer.length [1]: "+buffer2[1]);
                                int i;
                                for (i = 0; i < buffer.length; i++) {
                                    buffer2[i+1] = buffer[i];
                                    System.out.println("buffer.length [i]: "+i+" = "+buffer[i]);
                                }
                                buffer2[buffer.length+1] = 0x03;

                                int bf =buffer.length+1;
                                System.out.println("buffer.length : "+ bf+" = "+buffer2[buffer.length+1]);
                                System.out.println("buffer.length : "+buffer);

                                tcpSocket.getOutputStream().write(buffer2);
                                tcpSocket.getOutputStream().flush();
                                //System.out.println("send message buffer"+ convert(buffer));
//                            }else{
//                                tcpSocket.getOutputStream().write(0x02);
//                                tcpSocket.getOutputStream().write(buffer);
//                                tcpSocket.getOutputStream().write(0x03);
//                                tcpSocket.getOutputStream().flush();
//                            }

                            }
                            catch (IOException e)
                            {
                                Intent intent2 = new Intent("tcf.linkStatus");
                                if(tcpSocket!=null)
                                    intent2.putExtra("status",tcpSocket.isConnected());
                                //sendBroadcast(intent2);
                                IsConnected.set(false);

                                try {
                                    if(tcpSocket!=null)
                                        tcpSocket.close();
                                }
                                catch (IOException e1)
                                {

                                }
                                //if(tcpSocket!=null)
                                //Log.i("service","not connected intent send isconnect"+tcpSocket.isConnected());
                                //Log.i("service","not connected intent send interrupted"+IsInterrupted);
                                //Log.i("service","not connected intent send tcpsocket "+tcpSocket);
                            }
                            //if(tcpSocket!=null)
                            //Log.i("service","not connected intent send else isconnect"+tcpSocket.isConnected());
                            //Log.i("service","not connected intent send else interrupted"+IsInterrupted);
                            //Log.i("service","not connected intent send else tcpsocket "+tcpSocket);
                        }
                        //read message
                        try {
                            if(tcpSocket!=null)
                                if (tcpSocket.getInputStream().available() >0)
                                {
                                    //reading and set to buffer
                                    byte[] buffer = new byte[tcpSocket.getInputStream().available()];
                                    tcpSocket.getInputStream().read(buffer);

                                    ArrayList<String> s = getFrame(buffer);
                                    if (!s.isEmpty())
                                    {
                                        System.out.println("s >>>"+s);
                                        for (String str: s
                                                ) {
                                            parser.Parse(str);

                                        }
                                    }
                                }
                        }
                        catch (IOException e)
                        {

                            Intent intent2 = new Intent("tcf.linkStatus");
                            if(tcpSocket!=null)
                                intent2.putExtra("status",tcpSocket.isConnected());
                            //sendBroadcast(intent2);
                            IsConnected.set(false);
                            try {
                                if(tcpSocket!=null)
                                    tcpSocket.close();
                            }
                            catch (IOException e1)
                            {

                            }
                            //if(tcpSocket!=null)
                            //Log.i("service","not connected intent read isconnected"+tcpSocket.isConnected());
                            //Log.i("service","not connected intent read isinterrupted"+IsInterrupted);
                            //Log.i("service","not connected intent read tcpsocket "+tcpSocket);
                        }
                        //check status
//                    Log.i("service","not connected intent read else isconnected"+tcpSocket.isConnected());
//                    Log.i("service","not connected intent read else isinterrupted"+IsInterrupted);
//                    Log.i("service","not connected intent read else tcpsocket "+tcpSocket);
//                    Intent intent = new Intent("tcf.linkStatus");
//                    intent.putExtra("status",tcpSocket.isConnected());
//                    sendBroadcast(intent);
////                    IsConnected.set(true);
//                    if(!tcpSocket.isConnected()) {
//                        try {
//                            tcpSocket.close();
//                        } catch (IOException e1) {
//
//                        }
//                    }
                    }
            }
        }

        String currentBuffer = "";
        boolean isStartFound = false;
        private ArrayList<String> getFrame(byte[] data)
        {
            int startIdx = -1;
            int endIdx = -1;
            ArrayList<String> strList = new ArrayList<>();
            if (data.length > 0)
            {
                for (int i =0;i<data.length;i++)
                {
                    if (isStartFound && data[i] != 0x02 && data[i] != 0x03)
                    {
                        currentBuffer+= (char) data[i];
                    }
                    else
                    {
                        if (data[i] == 0x02)
                        {
                            isStartFound = true;
                            currentBuffer = "";
                        }
                        else  if (data[i] == 0x03)
                        {
                            isStartFound = false;
                            strList.add(currentBuffer);
                        }
                    }
                }
            }
            return  strList;
        }
    };

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(Byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    String convert(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length);
        for (int i = 0; i < data.length; ++ i) {
            if (data[i] < 0) throw new IllegalArgumentException();
            sb.append((char) data[i]);
        }
        return sb.toString();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
