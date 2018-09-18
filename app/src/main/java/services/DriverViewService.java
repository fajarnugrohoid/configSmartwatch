package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

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

import message.EventMessage;
import message.SMS;
import model.ModelMessage;
import parser.LocalDataParser;


/**
 * Created by user on 19/04/2018.
 */

public class DriverViewService extends Service {
    private static final String LOG_TAG = "smartwatch-driverview";
    public static final String CUSTOM_INTENT = "MainActivity";
    Context context;

    public static final int serverPort = 6666; //port driverview

    public static boolean forFlag = true;
    public static boolean finishFlag = false;
    public static DatagramSocket socket;

    public static String[] arrString = new String[300];
    public static int cntString;
    public static String[] extraIntent = new String[300];

    //private String ipAddress = "192.168.30.100"; //ip dari dari unit kontrol
    private String ipAddress = "192.168.30.200"; //ipdriverview dari unit terminal (display)
    //private String ipAddress = "192.168.0.2";

    public static boolean isIpOk;
    //private GeoDatabase mGeoDatabase;x`
    public static boolean ismanloc;
    public static double mlat, mlon;

    private static final String TAG = "MyService";
    private boolean isRunning  = false;
    private String statusConnect = "false";
    private Looper looper;
    EventBus eventBus;

    public  static ConcurrentLinkedQueue<ModelMessage> queue = new ConcurrentLinkedQueue<>();

    final Handler mHandler = new Handler();
    private Thread mUiThread;

    @Override
    public void onCreate() {
        /*HandlerThread handlerthread = new HandlerThread("MyThread", Process.);
        handlerthread.start();
        looper = handlerthread.getLooper(); */
        //myServiceHandler = new MyServiceHandler(looper);
        Log.i("xxx", "onCreate driverviewService");
        new Thread(new InitThread()).start();
        isRunning = true;
    }

    public int onStartCommand(Intent i, int flags, int startId){

        Log.i("xxx", "on Start Comamnd DaService2");

        Log.d(LOG_TAG, "driverviewService started.");

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
            //Log.d(LOG_TAG, "driverviewService stopped.");
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

                        statusConnect = "true";
                        //sendMessageStatus(statusConnect);

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
                        Thread tRead = new Thread(read);
                        tRead.start();

                        Consumer consumer = new Consumer(queue);
                        Thread tConsumer = new Thread(consumer);
                        tConsumer.start();
                        //new Thread(new ReadThread()).start();

                    }else{
                        statusConnect = "false";
                        //sendMessageStatus(statusConnect);

                        Log.i(LOG_TAG, ipAddress + " unreacable");
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "InitThread--" + e.getMessage());
            }
        }
    }


    public void sendMessageStatus(final String statusConnect) {
        runOnUiThread(new Runnable() {
            public void run() {
                // use data here
                EventBus.getDefault().post(statusConnect);
            }
        });
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }


    public class ReadThread implements Runnable {

        private StringBuilder sb = new StringBuilder();
        private boolean isnttimeout = true;
        private String byteInput;
        LocalDataParser dataParser = new LocalDataParser();
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
                        Log.d(TAG, "isnttimeout statusconnect:"+ isnttimeout);
                        socket.receive(packet); // This method blocks until
                        // a packet is received or a
                        // timeout has expired
                    } catch (SocketTimeoutException e) {
                        isnttimeout = false;
                        Log.e(LOG_TAG, "Timeout");
                        statusConnect = "false";
                        //sendMessageStatus(statusConnect);
                    }

                    if (isnttimeout) {
                        statusConnect = "true";
                        //sendMessageStatus(statusConnect);

                        Log.d(LOG_TAG,"packet: "+packet.toString());
                        String packetString = "";

                            packetString = new String(packet.getData(),0,packet.getLength());
                            Log.d(LOG_TAG,"packetString driverview:"+ packetString );
                            /*
                            int countBytesRead = packet.getLength();
                            Log.d(LOG_TAG,"countBytesRead: "+countBytesRead);

                            for (int i = 0; i < countBytesRead; i++) {
                                byteInput = String.format(" %02x", buffer[i]);
                                sb.append(byteInput.trim()); sb.append(" ");
                            }

                            Log.d(LOG_TAG, "sb driverview:" + sb.toString());
                            sb.delete(0, sb.length());
                            Log.i(LOG_TAG, "buffer:" + buffer);
                            packetString = dataParser.parseSbcSentence(buffer);
                            Log.i(LOG_TAG, "packetString ccu : " + packetString);
                            */
                            //// then send by intent broadcast
                            //EventBus.getDefault().post(new EventMessage(parseSbcSentence));

                        produce(packetString);

                        Log.i(LOG_TAG, "cntString: "+cntString);
                        for (int i = 0; i < cntString; i++) {
                            Intent it = new Intent();

                            it.putExtra(extraIntent[i], arrString[i]);
                            it.setAction(CUSTOM_INTENT);
                            context.sendBroadcast(it);

                            //STart
                            String msg = "messageb;"  + ","  + arrString[i];
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
                    Log.d(LOG_TAG, "arrPortName[0]:" + arrPortName[0]);
                    Log.d(LOG_TAG, "arrPortName[1]:" + arrPortName[1]);
                    Log.d(LOG_TAG, "arrParsed[0]:" + arrParsed[0]);
                    Log.d(LOG_TAG, "arrParsed[1]:" + arrParsed[1]);
                    if (arrPortName[0].equalsIgnoreCase("local")){
                        if (arrParsed[0].equalsIgnoreCase("000")) {
                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[4]);
                                Long = Double.parseDouble(arrParsed[5]);
                            }

                            int friendId = Integer.parseInt(arrParsed[1]);

                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            System.out.println("Producer local: " + m);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }

                            int qsize =queue.size();
                            System.out.println("qsize:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint local:" + elm.getMessage());
                            }

//                            Consumer con = new Consumer(queue);
//                            Thread t2 = new Thread(con);
//                            t2.start();

                        }else if (arrParsed[0].equalsIgnoreCase("006")) {

                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], Integer.parseInt(arrParsed[1]), Lat, Long, msg);
                            System.out.println("Producer local del skenario: " + m);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }

                        }
                    }else if (arrPortName[0].equalsIgnoreCase("child")){
                        if (arrParsed[0].equalsIgnoreCase("000")) {
                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[4]);
                                Long = Double.parseDouble(arrParsed[5]);
                            }

                            int friendId = Integer.parseInt(arrParsed[1]);
                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            System.out.println("Producer child : " + m);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }

                            int qsize =queue.size();
                            System.out.println("qsize child:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint child:" + elm.getMessage());
                            }
//                            Consumer con = new Consumer(queue);
//                            Thread t2 = new Thread(con);
//                            t2.start();
                        }
                    }
                    else if (arrPortName[0].equalsIgnoreCase("neigh")){
                        if (arrParsed[0].equalsIgnoreCase("000")) {
                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[4]);
                                Long = Double.parseDouble(arrParsed[5]);
                            }

                            int friendId = Integer.parseInt(arrParsed[1]);
                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            System.out.println("Producer neigh: " + m);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }

                            int qsize =queue.size();
                            System.out.println("qsize neigh:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint neigh:" + elm.getMessage());
                            }
//                            Consumer con = new Consumer(queue);
//                            Thread t2 = new Thread(con);
//                            t2.start();
                        }
                    }else if (arrPortName[0].equalsIgnoreCase("bcast")){
                        if ((arrParsed[0].equalsIgnoreCase("006")) || (arrParsed[0].equalsIgnoreCase("000"))) {
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
                            System.out.println("Producer bcast: " + m);
                            int qsize =queue.size();
                            System.out.println("qsize:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint bcast:" + elm.getMessage());
                            }

//                            Consumer con = new Consumer(queue);
//                            Thread t2 = new Thread(con);
//                            t2.start();
                        }

                        if (arrParsed[0].equalsIgnoreCase("004")) {
                            //jika skenario simbol taktis
                            Log.d(LOG_TAG, "arrPortName[0]:" + arrPortName[0]);
//                            for (int i = 0; i < arrParsed.length; i++) {
//                                Lat = Double.parseDouble(arrParsed[4]);
//                                Long = Double.parseDouble(arrParsed[5]);
//                            }
                            //bcast;004,65535,39,3,Ãabcdef,1,desc,4,-6.91484,107.362352,-6.915238,107.468248,-6.87643,107.464528,-6.880324,107.460184
                            int friendId = Integer.parseInt(arrParsed[1]);
                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }
                            System.out.println("Producer bcast skenario: " + m);
                            int qsize =queue.size();
                            System.out.println("qsize bcast skenario:" + qsize);
                            //printall
                            Iterator<ModelMessage> itr = queue.iterator();
                            while (itr.hasNext()) {
                                ModelMessage elm = itr.next();
                                System.out.println("Producerprint bcast skenario:" + elm.getMessage());
                            }

//                            Consumer con = new Consumer(queue);
//                            t2 = new Thread(con);
//                            t2.start();
                        }
                    }else if (arrPortName[0].equalsIgnoreCase("trap")) {
                        if (arrParsed[0].equalsIgnoreCase("003")) {

                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[2]);
                                Long = Double.parseDouble(arrParsed[3]);
                            }
                            //int friendId = Integer.parseInt(arrParsed[1]);
                            int friendId = Integer.parseInt(arrParsed[1]);
                            ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], friendId, Lat, Long, msg);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }
                        }
                    }
                } //else of check arrParsed
                else{
                    if (arrPortName[0].equalsIgnoreCase("local")){
                        if (arrParsed[0].equalsIgnoreCase("019")) {
                            //packetString driverview:local;019,0~38~0~1~5b922287~testsms~38
                            /*ModelMessage m = new ModelMessage(ctr++, arrPortName[0], arrParsed[0], 0, 0.0, 0.0, msg);
                            System.out.println("Producer local sms : " + m);
                            queue.add(m);
                            synchronized (queue) {
                                queue.notifyAll();
                            }*/
                            sendMessageStatus(msg);
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
                    System.out.println("Consumer: " + m.toString());
                    EventBus.getDefault().post(new EventMessage(m));
                }
            }
        }
    }

    @Subscribe
    public void onEvent(EventMessage event)
    {
    }

    @Subscribe
    public void onSendMessage(String strMessage)
    {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
