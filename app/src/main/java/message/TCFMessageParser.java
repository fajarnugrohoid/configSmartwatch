package message;

import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by user on 19/04/2018.
 */

public class TCFMessageParser {
    ConcurrentLinkedQueue<TCFMessage> dataQueue = null;

    public TCFMessageParser(ConcurrentLinkedQueue<TCFMessage> MessageQueue)
    {
        dataQueue = MessageQueue;
    }

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

    public void Parse(String data)
    {
        byte[] arr = hexStringToByteArray(data);
        if (arr.length == 0) return;
        switch (arr[0])
//        switch (arr[1]) // addbyme
        {
            /*case 0x02 : ParseShooter(arr); break;
            case 0x01 : ParseController(arr); break;
            case 0x03 : ParseObserver(arr); break;
            case 0x04 : ParseText(arr); break;
            case 0x05 : ParseFireRequest(arr); break;
            case 0x06 : ParseFireReply(arr); break;
            case 0x0A : ParseFireOrder(arr); break; */
            case 0x07 : ParseTacticalObject(arr); System.out.println("object masuk parese");break;
            /*case 0x08 : ParseWeather(arr); break;
            case 0x09 : ParseImageData(arr); break;
            //case 0x0C : ParseTime(arr); break;
            case 0x0D : ParseBattery(arr); break;
            case 0x0C : ParseForCheckCCUDevice(arr); break;
            case 0x0F : ParseSyncFireRequest(arr); break; */
        }
    }

    private  void ParseTacticalObject(byte[] data)
    {
//        
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public   static void addByteList(ArrayList<Byte> arr, byte[] data)
    {
        for (byte b:data
                ) {
            arr.add(b);
        }
    }

    public   static void addStringList(ArrayList<Byte> arr, String data)
    {
        for (char b:data.toCharArray()
                ) {
            arr.add((byte)b);
        }
    }

    public static byte[] DoubletoByteArray(double value) {
        DoubletoByteArrayByMe(value);
        byte[] bytes = new byte[8];
//        ByteBuffer.wrap(bytes).putDouble(value);
//        return bytes;
        ByteBuffer.wrap(bytes).putDouble(value);
        byte[] b2 = new byte[]{bytes[7],bytes[6],bytes[5],bytes[4],bytes[3],bytes[2],bytes[1],bytes[0]};
        byte[] b3 = new byte[]{bytes[0],bytes[1],bytes[2],bytes[3],bytes[4],bytes[5],bytes[6],bytes[7]};
        Double d = arr2Double2(b2);
        Double d2 = arr2Double2(b3);
        Log.i("doubletobyte"," "+b2+" d  "+d+" value "+value);
        Log.i("doubletobyte"," "+b3+" d2 "+d2+" value "+value);
        return b2;
    }

    public static byte[] DoubletoByteArray4(double value) {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putDouble(value);
        byte[] b2 = new byte[]{bytes[3],bytes[2],bytes[1],bytes[0]};
        return b2;
    }

    public static Double arr2Double2(byte[] Data)
    {
        byte[] arr = {Data[7],Data[6],Data[5],Data[4],Data[3],Data[2],Data[1],Data[0]};
        double i =  ByteArraytoDouble(arr);
        return i;
    }

    public static byte[] DoubletoByteArrayByMe(double value) {
        byte[] output = new byte[8];
        long lng = Double.doubleToLongBits(value);
        for(int i = 0; i < 8; i++) output[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);
        byte[] b2 = new byte[]{output[7],output[6],output[5],output[4],output[3],output[2],output[1],output[0]};
        for(int i = 0; i < 8; i++) b2[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);
        Double outputDouble = arr2Double2(output);
        Double b2Double = arr2Double2(b2);
        Log.i("doubletobyte"," output "+output+" outputdouble "+outputDouble+" value "+value);
        Log.i("doubletobyte"," outtb2  "+b2+" b2double "+b2Double+" value "+value);
        return output;
    }

    public static double ByteArraytoDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static  byte[] InttoByteArray(int myInteger){
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
    }

    public static  byte[] WordtoByteArray(int myInteger){
        Short s = new Short((short) myInteger);
//        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort((short) myInteger).array();
        byte[] b = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) myInteger).array();
        //byte[] b2 = new byte[]{b[1],b[0]};
        //System.out.println("fire "+" b2 = "+bytesToHex(toObjects(b2))+" b = "+bytesToHex(toObjects(b)));
        return b;
    }

    public static int ByteArraytoInteger(byte [] byteBarray){
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static Pair<Integer,Integer> ByteArraytoInteger2(byte [] byteBarray, int Position){
        byte[] arr = {byteBarray[Position+4],byteBarray[Position+3],byteBarray[Position+2],byteBarray[Position+1],byteBarray[Position]};
        int i =  ByteArraytoInteger(arr);
        return Pair.create(new Integer(i),new Integer(Position+4));
//        return ByteBuffer.wrap(byteBarray).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static  byte[] LongtoByteArray(Long myLong){
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(myLong).array();
    }

    public static long ByteArraytoLong(byte [] byteBarray){
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public  static Pair<Integer,Integer> arr2Byte(byte[] Data, int Position)
    {
        int i =  (((int) Data[Position])&0x000000FF);
        return Pair.create(new Integer(i),new Integer(Position+1));
    }

    public static Pair<Integer,Integer> arr2Word(byte[] Data, int Position)
    {
        //int i =  (((int) Data[Position+1])<<8)|(((int) Data[Position])&0x000000FF);
        byte[] b = new byte[]{Data[Position],Data[Position+1]};
        int i = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return Pair.create(new Integer(i),new Integer(Position+2));
    }

    public static Pair<Long,Integer> arr2Dword(byte[] Data, int Position)
    {
        //long i =  (((long) Data[Position+3])<<24)|(((long) Data[Position+2])<<16)|(((long) Data[Position+1])<<8)|(((long) Data[Position])&0x000000FF);
        byte[] b = new byte[]{Data[Position],Data[Position+1],Data[Position+2],Data[Position+3]};
        long i = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return Pair.create(new Long(i),new Integer(Position+4));
    }

    public static Pair<Long,Integer> arr2Long(byte[] Data, int Position)
    {
        byte[] arr = {Data[Position],Data[Position+1],Data[Position+2],Data[Position+3],Data[Position+4],Data[Position+5],Data[Position+6],Data[Position+7]};
        long i =  ByteArraytoLong(arr);
        return Pair.create(new Long(i),new Integer(Position+8));
    }

//    public static Pair<Double,Integer> arr2Double(byte[] Data, int Position)
//    {
//        byte[] arr = {Data[Position],Data[Position+1],Data[Position+2],Data[Position+3],Data[Position+4],Data[Position+5],Data[Position+6],Data[Position+7]};
//        double i =  ByteArraytoDouble(arr);
//        return Pair.create(new Double(i),new Integer(Position+4));
//    }

    public static Pair<Double,Integer> arr2Double(byte[] Data, int Position)
    {
        byte[] arr = {Data[Position+7],Data[Position+6],Data[Position+5],Data[Position+4],Data[Position+3],Data[Position+2],Data[Position+1],Data[Position]};
        double i =  ByteArraytoDouble(arr);
        return Pair.create(new Double(i),new Integer(Position+8));
    }


    //byMe
    private  Byte[] check(ArrayList<Byte> arr ){
        byte[] result1 = new byte[arr.size()];
        for(int i = 0; i < arr.size(); i++) {
            result1[i] = arr.get(i).byteValue();
        }
        Byte[] data1 = new Byte[result1.length] ;
        for(int i = 0;i<result1.length;i++)
        {
            data1[i] = result1[i];
        }
        return data1;
    }

    private Byte[] checkParse(byte[] data){
        Byte[] data2 = new Byte[data.length] ;
        for(int i = 0;i<data.length;i++)
        {
            data2[i] = data[i];
        }
        System.out.println("ParseFireReply >> bytesToHex : "+bytesToHex(data2));
        return data2;
    }

    // byte[] to Byte[]
    private static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing

        return bytes;
    }

    private static String ShorttoBase64(Short data)
    {
        //byte[] b;

        byte[] b = ByteBuffer.allocate(2).putShort(data).array();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    String toBinary(byte[] bytes )
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for(int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public  static  void latlon2Array(ArrayList<Byte> list, double coord)
    {
        double newCoord = coord * 1000000.0;
        int i = (int) (Math.round(newCoord));
        byte[] b = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
        for (byte c:b
                ) {
            list.add(c);
        }
    }

    public  static Pair<Double,Integer> arr2latlon(byte[] Data, int Position )
    {
        byte[] arr = {Data[Position], Data[Position+1], Data[Position+2], Data[Position+3]};
        int i =  ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getInt();
        double coord = (double)(i/1000000.0);
        return Pair.create(new Double(coord),new Integer(Position+4));
    }

    public  static void kompas2Array(ArrayList<Byte> list, double mils)
    {
        double angle = mils * 5.0;
        short i = (short)(Math.round(angle));
        byte[] b = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(i).array();
        for (byte c:b
                ) {
            list.add(c);
        }
    }

    public  static Pair<Double,Integer> arr2kompas(byte[] Data, int Position )
    {
        byte[] arr = {Data[Position], Data[Position+1]};
        short i =  ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).getShort();
        double coord = (double)(i/5.0);
        return Pair.create(new Double(coord),new Integer(Position+2));
    }

    private void check(Byte[] data){
        Byte[] data2 = new Byte[data.length] ;
        for(int i = 0;i<data.length;i++)
        {
            data2[i] = data[i];
        }
        System.out.println("ParseFireRequest >> bytesToHex : "+bytesToHex(data2));
    }
}
