package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import model.ModelMessage;
import parser.ChildDataParser;
import message.EventMessage;
import com.bms.user.bmssmartwatch.SBCAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 30/04/2018.
 */

public class ChildService extends Service {
    private static final String LOG_TAG = "smartwatch-child";
    Context context;
    public static final int serverPort = 9003;

    public static boolean forFlag = true;
    public static boolean finishFlag = false;
    public static DatagramSocket socket;

    public static String[] arrString = new String[300];
    public static int cntString;
    public static String[] extraIntent = new String[300];

    private String ipAddress = "192.168.30.100";
    //private String ipAddress = "192.168.66.90";

    public static boolean isIpOk;
    //private GeoDatabase mGeoDatabase;x`
    public static boolean ismanloc;
    public static double mlat, mlon;

    private static final String TAG = "smartwatch-child";
    private boolean isRunning  = false;
    private Looper looper;
    EventBus eventBus;
    public  static ConcurrentLinkedQueue<ModelMessage> queue = new ConcurrentLinkedQueue<>();


    @Override
    public void onCreate() {
        Log.i("xxx", "ChildService onCreate");
        new Thread(new ChildService.InitThread()).start();
        isRunning = true;
    }

    public int onStartCommand(Intent i, int flags, int startId){

        Log.i("xxx", "ChildService on Start Comamnd");

        Log.d(LOG_TAG, "ChildService LocalService started.");
        isIpOk = false;
        context = this;

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

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

        new Thread(new ChildService.InitThread()).start();

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

                        try{
                            Log.i(LOG_TAG, "IP Corrected");
                            //socket.setReuseAddress(true);
                            socket = new DatagramSocket(serverPort);
                            Log.i(LOG_TAG, "IP socket");
                            InetAddress serverAddr = InetAddress.getByName(ipAddress);
                            Log.i(LOG_TAG, "IP InetAddress serverAddr " + serverAddr);
                            byte[] buf = ("UDP FILES  FriendService ").getBytes();
                            DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, serverPort);
                            Log.i(LOG_TAG, "IP packet " + packet);
                            socket.send(packet);
                            isIpOk = true;
                            socket.setSoTimeout(60000);
                            //new Thread(new ChildService.ReadThread(queue)).start();
                            ReadThread read = new ReadThread(queue);
                            Thread t1 = new Thread(read);
                            t1.start();

                        }catch (IOException e){
                            Log.e(LOG_TAG, "Error Connection " + e.toString());
                        }


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
        ChildDataParser dataParser = new ChildDataParser();
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

                        Log.d(LOG_TAG,"packet: "+packet);
                        int countBytesRead = packet.getLength();
                        Log.d(LOG_TAG,"countBytesRead: "+countBytesRead);

                        for (int i = 0; i < countBytesRead; i++) {
                            byteInput = String.format(" %02x", buffer[i]);
                            sb.append(byteInput.trim()); sb.append(" ");
                        }

                        Log.d(LOG_TAG, "sb child:" + sb.toString());
                        sb.delete(0, sb.length());
                        Log.i(LOG_TAG, "buffer:" + buffer);
                        String packetString = dataParser.parseSbcSentence(buffer);
                        Log.i(LOG_TAG, "parseSbcSentence child:" + packetString);
                        //// then send by intent broadcast
                        produce(packetString);



                        //EventBus.getDefault().post(new EventMessage(parseSbcSentence));
                        Log.i(LOG_TAG, "cntString: "+cntString);

                        for (int i = 0; i < cntString; i++) {
                            //Start
                            String msg = "child;"  + ","  + arrString[i];
                            Log.i(LOG_TAG, "msg:" + msg);
                            final Handler toastHandler = new Handler();
                            byte[] msgBytes = msg.getBytes();
                            msgBytes = msg.getBytes();

                            final byte[] buf = msgBytes;

                            new Thread(new Runnable() {
                                public void run() {


                                    try {
                                        InetAddress serverAddress = InetAddress.getByName(ipAddress);
                                        //Log.v(getString(R.string.app_name), serverAddress.getHostAddress());
                                        Log.i(LOG_TAG, serverAddress.getHostAddress());

                                        DatagramSocket socket = new DatagramSocket();
                                        if (!socket.getBroadcast()) socket.setBroadcast(true);
                                        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                                                serverAddress, serverPort);
                                        socket.send(packet);
                                        socket.close();


                                    } catch (final UnknownHostException e) {
                                        toastHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(context, e.toString(),
                                                //              Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        e.printStackTrace();
                                    } catch (final SocketException e) {
                                        toastHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(mContext, e.toString(),
                                                //Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        e.printStackTrace();
                                    } catch (final IOException e) {
                                        toastHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(mContext, e.toString(),
                                                //Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        e.printStackTrace();
                                    }


                                    //Looper.myLooper().quit();

                                }
                            }).start();
                            //End

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
                    Log.d(LOG_TAG, "arrPortName child[0]:" + arrPortName[0]);
                    Log.d(LOG_TAG, "arrPortName child[1]:" + arrPortName[1]);
                    Log.d(LOG_TAG, "arrParsed child[0]:" + arrParsed[0]);
                    Log.d(LOG_TAG, "arrParsed child[1]:" + arrParsed[1]);
                    Log.d(LOG_TAG, "arrParsed child[2]:" + arrParsed[2]);
                    Log.d(LOG_TAG, "arrParsed child[3]:" + arrParsed[3]);
                    Log.d(LOG_TAG, "arrParsed child[4]:" + arrParsed[4]);
                    Log.d(LOG_TAG, "arrParsed child[5]:" + arrParsed[5]);
                    Log.d(LOG_TAG, "arrParsed child[6]:" + arrParsed[6]);
                    Log.d(LOG_TAG, "arrParsed child[7]:" + arrParsed[7]);
                    Log.d(LOG_TAG, "arrParsed child[8]:" + arrParsed[8]);

                    if (arrPortName[0].equalsIgnoreCase("child")){
                        if (arrParsed[0].equalsIgnoreCase("000")) {
                            for (int i = 0; i < arrParsed.length; i++) {
                                Lat = Double.parseDouble(arrParsed[6]);
                                Long = Double.parseDouble(arrParsed[7]);
                            }
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
                            Thread t = new Thread(con);
                            t.start();

                        }



                    }

                } //end of check arrParsed

            }//endof check arrPortNameconsume
        }

        private void consume(){
            while (!queue.isEmpty()) {
                ModelMessage m = queue.poll();
                if (m != null) {
                    System.out.println("Consumer: " + m.toString());
                }
            }
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
                consume();
                try {
                    synchronized (queue) {
                        queue.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BroadcastService.Consumer.class.getName()).log(Level.SEVERE, null, ex);
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
        Log.i(TAG, "dataService child :" + event.getMessageQueue());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
