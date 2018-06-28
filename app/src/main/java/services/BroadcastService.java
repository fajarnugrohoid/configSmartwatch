package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import message.EventMessage;
import com.bms.user.bmssmartwatch.MainActivity;
import com.bms.user.bmssmartwatch.SBCAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ModelMessage;
import parser.BroadcastDataParser;


/**
 * Created by user on 19/04/2018.
 */

public class BroadcastService extends Service {
    private static final String LOG_TAG = "smartwatch-broadcast";
    public static final String CUSTOM_INTENT = "MainActivity";
    Context context;
    public static final int serverPort = 9001;

    public static boolean forFlag = true;
    public static boolean finishFlag = false;
    public static DatagramSocket socket;

    public static String[] arrString = new String[300];
    public static int cntString;
    public static String[] extraIntent = new String[300];

    private String ipAddress = "192.168.30.100";
    //private String ipAddress = "192.168.0.248";
    //private String ipAddress = "192.168.14.152";
    //private String ipAddress = "192.168.66.90";

    public static boolean isIpOk;
    //private GeoDatabase mGeoDatabase;x`
    public static boolean ismanloc;
    public static double mlat, mlon;

    private static final String TAG = "smartwatch-broadcast";
    private boolean isRunning  = false;
    private Looper looper;
    EventBus eventBus;

    public  static ConcurrentLinkedQueue<ModelMessage> queue = new ConcurrentLinkedQueue<>();
    //Queue<ModelMessage> queue = new ConcurrentLinkedQueue<ModelMessage>();


    @Override
    public void onCreate() {
        /*HandlerThread handlerthread = new HandlerThread("MyThread", Process.);
        handlerthread.start();
        looper = handlerthread.getLooper(); */
        //myServiceHandler = new MyServiceHandler(looper);
        Log.i("xxx", "onCreate Broadcast");
        new Thread(new InitThread()).start();
        isRunning = true;
    }

    public int onStartCommand(Intent i, int flags, int startId){

        Log.i("xxx", "on Start Comamnd DaService2");

        Log.d(LOG_TAG, "LocalService started.");

        //// cek manual location
        //mGeoDatabase = new GeoDatabase(getApplicationContext());
        //ismanloc = mGeoDatabase.isManLoc();
        //Log.d(LOG_TAG, "isManLoc: " + ismanloc);

        /*if (ismanloc) {
            String valPos = mGeoDatabase.getLoc();
            SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(
                    ',');
            splitter.setString(valPos);
            mlat = Double.parseDouble(splitter.next()) - 0.0001;
            mlon = Double.parseDouble(splitter.next()) - 0.0001;
        }
        mGeoDatabase.FreeDatabases();*/

        isIpOk = false;
        context = this;

        //eventBus = ((SyncApp) getApplication()).getEventBus();
        //if(!eventBus.isRegistered(this))
        //    eventBus.register(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (finishFlag) {
            forFlag = false;

            if (socket != null) {
                Log.d(LOG_TAG, "closing socket");
                socket.close();
                Log.d(LOG_TAG, "socket closed.");
            }
            //Log.d(LOG_TAG, "LocalService stopped.");
            return Service.START_NOT_STICKY;
        }

        new Thread(new InitThread()).start();

        return Service.START_NOT_STICKY;
    }


    public class InitThread implements Runnable {

        public void run() {
            try {
                Log.d(LOG_TAG, "InitThread begins.");
                // connect to socket
                Log.i(LOG_TAG, "checking ip...");
                Log.i(LOG_TAG, "xxx IP Address:" + ipAddress );
                Log.i(LOG_TAG, "xxx isIpOk2:" + isIpOk );

                while(!isIpOk){
                    if(SBCAdapter.checkSbcAddress(ipAddress)){

                        Log.i(LOG_TAG, "Your IP Corrected");
                        //socket.setReuseAddress(true);
                        socket = new DatagramSocket(serverPort);
                        InetAddress serverAddr = InetAddress.getByName(ipAddress);
                        Log.i(LOG_TAG, "IP InetAddress serverAddr " + serverAddr);
                        byte[] buf = ("UDP FILES  xxxrrr  hh gg sdad").getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, serverPort);
                        Log.i(LOG_TAG, "IP packet " + packet);
                        socket.send(packet);
                        isIpOk = true;
                        socket.setSoTimeout(60000);

                        ReadThread read = new ReadThread(queue);
                        Thread t1 = new Thread(read);
                        t1.start();
                        //new Thread(new ReadThread()).start();

                    }else{
                        Log.i(LOG_TAG, ipAddress + " unreacable");
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "InitThread--" + e.getMessage());
            }
        }
    }

    public class ReadThread implements Runnable {

        private StringBuilder sb = new StringBuilder();
        private boolean isnttimeout = true;
        private String byteInput;
        BroadcastDataParser dataParser = new BroadcastDataParser();
        private byte[] buffer = new byte[5000];

        private int ctr;
        private ConcurrentLinkedQueue<ModelMessage> queue;

        ReadThread(ConcurrentLinkedQueue<ModelMessage> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            // read data
            Looper.prepare();
            Log.i(LOG_TAG, "Read thread begins.");
            while (forFlag) {

                java.util.Arrays.fill(buffer,(byte) 0);

                try {
                    DatagramPacket packet = new DatagramPacket(buffer,
                            buffer.length);
                    Log.d(LOG_TAG, "Waiting Data\n");
                    try {
                        socket.receive(packet); // This method blocks until
                        // a packet is received or a
                        // timeout has expired
                    } catch (SocketTimeoutException e) {
                        Log.e(LOG_TAG, "Timeout");
                        isnttimeout = false;

                    }
                    if (isnttimeout) {

                        Log.d(LOG_TAG,"packet: "+packet.toString());
                        String packetString = "";


                        if (MainActivity.driverView==true){
                            packetString = new String(packet.getData(),0,packet.getLength());
                            Log.d(LOG_TAG,"packetString driverview:"+ packetString );

                        }else{
                            int countBytesRead = packet.getLength();
                            Log.d(LOG_TAG,"countBytesRead: "+countBytesRead);

                            for (int i = 0; i < countBytesRead; i++) {
                                byteInput = String.format(" %02x", buffer[i]);
                                sb.append(byteInput.trim()); sb.append(" ");
                            }

                            Log.d(LOG_TAG, "sb bcast:" + sb.toString());
                            sb.delete(0, sb.length());
                            Log.i(LOG_TAG, "buffer:" + buffer);
                            packetString = dataParser.parseSbcSentence(buffer);
                            Log.i(LOG_TAG, "packetString ccu broadcast : " + packetString);
                            //// then send by intent broadcast
                            //EventBus.getDefault().post(new EventMessage(parseSbcSentence));
                        }

                        produce(packetString);




                        Log.i(LOG_TAG, "cntString: "+cntString);
                        for (int i = 0; i < cntString; i++) {
                            Intent it = new Intent();

                            it.putExtra(extraIntent[i], arrString[i]);
                            it.setAction(CUSTOM_INTENT);
                            context.sendBroadcast(it);

                            String msg = "message;"  + ","  + arrString[i];
                            Log.i(LOG_TAG, "msg:" + msg);
                            final Handler toastHandler = new Handler();

                            try {
                                Thread.sleep(25);
                                //25
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                    isnttimeout = true;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "error while getting data", e);
                }
            }
            Looper.loop();
        }

        private void produce(String msg) {
            String[] arrPortName = msg.split(";",-1);
            if (arrPortName.length > 1){
                String[] arrParsed = arrPortName[1].split(",",-1);

                if (arrParsed.length > 2){
                    Double Lat = 0.0, Long=0.0;
                    Log.d(LOG_TAG, "arrPortName[0] bcast:" + arrPortName[0]);
                    Log.d(LOG_TAG, "arrPortName[1] bcast:" + arrPortName[1]);
                    Log.d(LOG_TAG, "arrParsed[0] bcast:" + arrParsed[0]);
                    Log.d(LOG_TAG, "arrParsed[1] bcast:" + arrParsed[1]);
                    Log.d(LOG_TAG, "arrParsed[2] bcast:" + arrParsed[2]);
                    Log.d(LOG_TAG, "arrParsed[3] bcast:" + arrParsed[3]);
                    Log.d(LOG_TAG, "arrParsed[4] bcast:" + arrParsed[4]);
                    Log.d(LOG_TAG, "arrParsed[5] bcast:" + arrParsed[5]);
                    Log.d(LOG_TAG, "arrParsed[6] bcast:" + arrParsed[6]);
                    if (arrPortName[0].equalsIgnoreCase("bcast")){
                        if (arrParsed[0].equalsIgnoreCase("000")) {
                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[4]);
                                Long = Double.parseDouble(arrParsed[5]);
                            }
                            //int friendId = Integer.parseInt(arrParsed[1]);
                            int friendId = Integer.parseInt(arrParsed[1]);
                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }
                            System.out.println("Producer: " + m);
                            int qsize =queue.size();
                            System.out.println("qsize:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint:" + elm.getMessage());
                            }

                            Consumer con = new Consumer(queue);
                            Thread t2 = new Thread(con);
                            t2.start();

                        }
                    }

                } //end of check arrParsed

            }//endof check arrPortName
        }

    }

    public class Consumer implements Runnable {

        private final ConcurrentLinkedQueue<ModelMessage> queue;

        public Consumer(ConcurrentLinkedQueue<ModelMessage> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {

                try {
                    synchronized (queue) {
                        consume();
                        queue.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void consume() {
            while (!queue.isEmpty()) {
                ModelMessage m = queue.poll();
                if (m != null) {
                    try {
                        queue.wait(1000);
                        EventBus.getDefault().post(new EventMessage(m));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Subscribe
    public void onEvent(EventMessage event)
    {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
