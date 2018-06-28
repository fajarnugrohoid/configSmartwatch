package parser;

/**
 * Created by user on 20/04/2018.
 */

import android.util.Log;

import gps.GPSLocation;
import gps.GPSLogistik;

import java.text.DecimalFormat;

public class ChildDataParser {
    private static final String LOG = "parser-child";
    public static int iLongitude, iLatitude, iHead1, iHead2, iBBLevel,
            iBBVolume, iTemp, iHum, iTimestamp, tHelp, iComm, iGpsQuality,
            iGpsValid, iDTime, iAmCannon, iAmGun,t;
    public static long iAddSrc ;
    private String strOid;

    public static double arlat[] = new double[120];
    public static double arlon[] = new double[120];
    private static byte[] bTime = new byte[4];

    private static double oldLat, oldLon;


    public String parseSbcSentence(byte[] buffer) throws SecurityException {

        String locationSentence = "child;";
        double lat = 0, lon = 0;
        float head1 = 0, head2 = 0, bbLevel = 0, bbVol = 0, temp = 0, humi = 0;
        int pduLength;
        int tHelp;
        int y = 0;
        int oid = 0;
        long address = 0;
        int cnt = 0;
        int master = 0;
        int base = 0;
        int status_active = 0;

        pduLength = buffer[5] & 0x000000ff;
        tHelp = buffer[6] & 0x0000007f;
        tHelp = tHelp << 8;
        pduLength |= tHelp;

        for (int x = 8; x < pduLength; x++) { // address for loop
            address = buffer[x] & 0x000000ff; // 8
            tHelp = buffer[++x] & 0x000000ff;
            tHelp = tHelp << 8; // 9
            address |= tHelp;
            tHelp = buffer[++x] & 0x000000ff;
            tHelp = tHelp << 16; // 9
            address |=tHelp;
            tHelp = buffer[++x] & 0x000000ff;
            tHelp = tHelp << 24; // 9
            address |=tHelp;
            Log.d(LOG, "Address : " + address);


            cnt = buffer[++x] & 0x000000ff; // 10
            tHelp = buffer[++x] & 0x000000ff;
            tHelp = tHelp << 8; // 11
            cnt |= tHelp;

            master = buffer[++x] & 0x000000ff; // 10

            //++x; // 12
            base = x;

            for (int z = 0; z < cnt; z++) { // oid for loop

                oid = buffer[++z + base] & 0x000000ff; // 13
                tHelp = buffer[++z + base] & 0x000000ff;
                tHelp = tHelp << 8; // 14
                oid |= tHelp;
                Log.d(LOG, "OID : " + oid);

                switch (oid) {

                    case 0xc102: // way point

                        break;

                    case 0xc101: // system status alarm mask
                        locationSentence += address + ",System Status alarm mask";
                        z += 4;
                        y = z;
                        break;

                    case 0xc100: // System Status
                        status_active = buffer[++z + base] & 0x000000ff; // 15
                        Log.d(LOG, "StatusActive : " + String.valueOf(status_active));
                        locationSentence += address + ",System Status," +status_active;
                        Log.d(LOG, "StatusActive : -->" + locationSentence);

                        z += 3;
                        y = z;
                        break;

                    case 0xc080: // frame resp
                        locationSentence += address + ",Frame Resp";
                        z += 2;
                        y = z;
                        break;

                    case 0xc0ff: // SMS
                        Log.d(LOG, "Child Entering sms parsing");

                        int cntSMS = buffer[++z + base] & 0x000000ff; // 15

                        t = z + cntSMS;

                        // timestamp(4)

                        iAddSrc = buffer[++z+base] & 0x000000ff; //17
                        tHelp = buffer[++z+base] & 0x000000ff; //18
                        tHelp = tHelp << 8;
                        iAddSrc |= tHelp;
                        tHelp = buffer[++z+base] & 0x000000ff;
                        tHelp = tHelp << 16;
                        iAddSrc |= tHelp;
                        tHelp = buffer[++z+base] & 0x000000ff;
                        tHelp = tHelp << 24;
                        iAddSrc |= tHelp;

                        cntSMS -= 2;

                        bTime[0] = buffer[++z+base];
                        bTime[1] = buffer[++z+base];
                        bTime[2] = buffer[++z+base];
                        bTime[3] = buffer[++z+base];

                        int codeSMS = buffer[++z + base] & 0x000000ff;
                        Log.d(LOG, "ignore sms report from Neigh = " + String.valueOf(codeSMS));

                        if (codeSMS == 225) {
                            Log.d(LOG, "ignore sms report from Neigh");
                            int iGroup = buffer[++z + base] & 0x000000ff; // 17

                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iGroup |= tHelp;

                            int iCntMessage = buffer[++z + base] & 0x000000ff;

                            String byteInputMessage;
                            StringBuilder sbMessage = new StringBuilder();
                            String STimeOrg = null;
                            try {
                                STimeOrg =getHexString(bTime);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            //* queue
                            /*SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(address);
                            sms.iAddSrc = String.valueOf(MainActivity.imyId);
                            sms.iGroup = String.valueOf(iGroup);
                            sms.STimeOrg = String.valueOf(STimeOrg);
                            sms.sbMessage = sbMessage.toString(); */

                            Log.v("Data Parser" , "DATA SMS DARI NEIGH : "+locationSentence);
                            //locationSentence = "ignore sms report from Neigh";
                            //MainActivity.UIQueue.add(Pair.create("SMSACK",(Object) sms));
                            //return locationSentence;

                        } else {
                            Log.d(LOG, "This is SMS");
                            int iGroup = buffer[++z + base] & 0x000000ff; // 17

                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iGroup |= tHelp;

                            int iCntMessage = buffer[++z + base] & 0x000000ff;

                            String byteInputMessage;
                            StringBuilder sbMessage = new StringBuilder();

                            for (int j = 0; j < iCntMessage; j++) {
                                sbMessage.append((char)buffer[++z+ base]);

                            }

                            String STimeOrg = null;
                            try {
                                STimeOrg =getHexString(bTime);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            locationSentence+=address+"~"+iAddSrc+"~"+iGroup+"~"+STimeOrg+
                                    "~"+sbMessage.toString();

                            //* queue
                            /*
                            SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(address);
                            sms.iAddSrc = String.valueOf(iAddSrc);
                            sms.iGroup = String.valueOf(iGroup);
                            sms.STimeOrg = String.valueOf(STimeOrg);
                            sms.sbMessage = sbMessage.toString();

                            MainActivity.UIQueue.add(Pair.create("SMS",(Object) sms)); */

                        }
                        z = t;
                        y = z;


                        break;

                    case 0xc103: // LSTRING
                        // Log.i(LOG, "Entering lstring parsing");
                        int cntLstring = buffer[++z + base]& 0x000000ff; // 15
                        tHelp = buffer[++z+base] & 0x000000ff; //18
                        tHelp = tHelp << 8;
                        cntLstring |= tHelp;


                        locationSentence += "LString";
                        Log.d(LOG, "cntLstring : " + cntLstring);

                        z += cntLstring;
                        y = z;
                        break;

                    case 0x07fe:
                        // link comm status (2)
                        int bGps = buffer[++z + base]; // 15

                        iComm = bGps & 0x1;
                        iGpsQuality = (bGps >> 4) & 7;
                        iGpsValid = (bGps >> 3) & 1;
                        ++z; // 16
                        // timestamp(4)
                        iDTime = buffer[++z + base] & 0x000000ff; // 17
                        tHelp = buffer[++z + base] & 0x000000ff; // 18
                        tHelp = tHelp << 8;
                        iDTime |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 16;
                        iDTime |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 24;
                        iDTime |= tHelp;
                        // latitude (4)
                        iLatitude = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iLatitude |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 16;
                        iLatitude |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 24;
                        iLatitude |= tHelp;
                        lat = this.getLatitude();
                        Log.d(LOG, "child: Latitude :~ " + lat);

                        // longitude (4)
                        iLongitude = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iLongitude |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 16;
                        iLongitude |= tHelp;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 24;
                        iLongitude |= tHelp;
                        lon = this.getLongitude();
                        Log.d(LOG, "child: Longitude :~ " + lon);

                        iHead1 = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iHead1 |= tHelp;
                        head1 = this.getHeading1();
                        iHead2 = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iHead2 |= tHelp;
                        head2 = this.getHeading2();

                        if(lat<=90 && lat>=-90) oldLat=lat;
                        if(lon<=180 && lon>=-180) oldLon=lon;

                        locationSentence += "000,"+address + "," + "s" + "," + iDTime + ","
                                + new DecimalFormat("#0.000000").format(oldLat) + ","
                                + new DecimalFormat("#0.000000").format(oldLon) + ","
                                + head1 + "," + head2 + "," + iComm + ","
                                + iGpsValid + "," + iGpsQuality;

                        //* queue
                        GPSLocation gpsstatus = new GPSLocation();
                        gpsstatus.address = String.valueOf(address);
                        gpsstatus.s = "s";
                        gpsstatus.iDTime = String.valueOf(iDTime);
                        gpsstatus.lat = String.valueOf(new DecimalFormat("#0.000000").format(oldLat));
                        gpsstatus.lon = String.valueOf(new DecimalFormat("#0.000000").format(oldLon));
                        gpsstatus.head1 = String.valueOf(head1);
                        gpsstatus.head2 = String.valueOf(head2);
                        gpsstatus.icomm = String.valueOf(iComm);
                        gpsstatus.iGpsValid = String.valueOf(iGpsValid);
                        gpsstatus.iGpsQuality = String.valueOf(iGpsQuality);
                       // MainActivity.UIQueue.add(Pair.create("GPSLOCATION",(Object) gpsstatus));
                        y = z;
                        break;
                    case 0x08ff:
                        // Log.d(LOG, "parsing SPV");
                        // BB Level
                        iBBLevel = buffer[++z + base] & 0x000000ff;

                        tHelp = buffer[++z + base] & 0x000000ff;

                        tHelp = tHelp << 8;
                        iBBLevel |= tHelp;
                        float fBBLevel = (float) iBBLevel;
                        //fBBLevel = fBBLevel / 100;

                        // AM Kanon
                        iAmCannon = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iAmCannon |= tHelp;

                        // AM Gun
                        iAmGun = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iAmGun |= tHelp;

                        // Temp
                        iTemp = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iTemp |= tHelp;
                        float fTemp = iTemp;
                        fTemp = fTemp / 10;

                        // hum
                        iHum = buffer[++z + base] & 0x000000ff;
                        tHelp = buffer[++z + base] & 0x000000ff;
                        tHelp = tHelp << 8;
                        iHum |= tHelp;
                        float fHum = iHum;
                        fHum = fHum / 10;

                        locationSentence += address + "," + "s" + "," + fBBLevel
                                + "," + iAmCannon + "," + iAmGun + "," + fTemp
                                + "," + fHum;

                        //* queue
                        GPSLogistik logistikstatus = new GPSLogistik();
                        logistikstatus.address = String.valueOf(address);
                        logistikstatus.s = "s";
                        logistikstatus.fBBLevel = String.valueOf(fBBLevel);
                        logistikstatus.iAmCannon = String.valueOf(iAmCannon);;
                        logistikstatus.iAmGun = String.valueOf(iAmGun);;
                        logistikstatus.fTemp = String.valueOf(fTemp);
                        logistikstatus.fHum = String.valueOf(fHum);

                        //MainActivity.UIQueue.add(Pair.create("LOGISTIKSTATUS",(Object) logistikstatus));

                        //*/
                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z;

                        y = z;
                        break;

                    default:
                        locationSentence += "No OID";
                        return locationSentence;

                } // end switch
            } // end of loop oid
            x = base + y;

        } // end of loop address

        return locationSentence;
    }

    public static double DmToDd(double dm) {
        double temp1 = Math.floor(dm);
        double temp2 = (dm - Math.floor(dm)) * 100;
        double Dd = temp1 + temp2 / 60;
        return Dd;
    }

    public static double getLatitude() {
        double realLat;
        if ((iLatitude & 0x80000000) == 0x80000000) {
            iLatitude &= 0x7fffffff;
            realLat = iLatitude / 1000000.0;
            realLat = DmToDd(realLat);
            realLat *= -1;
        } else {
            realLat = iLatitude / 1000000.0;
            realLat = DmToDd(realLat);
        }

        return realLat;
    }

    public static double getLongitude() {
        double realLon;
        if ((iLongitude & 0x80000000) == 0x80000000) {
            iLongitude &= 0x7fffffff;
            realLon = iLongitude / 1000000.0;
            realLon = DmToDd(realLon);
            realLon *= -1;
        } else {
            realLon = iLongitude / 1000000.0;
            realLon = DmToDd(realLon);
        }

        return realLon;
    }

    //
    public static double getLatitudeDD() {
        double realLat;
        if ((iLatitude & 0x80000000) == 0x80000000) {
            iLatitude &= 0x7fffffff;
            realLat = iLatitude / 10000.0;
            realLat *= -1;
        } else {
            realLat = iLatitude / 10000.0;
        }

        return realLat;
    }

    public static double getLongitudeDD() {
        double realLon;
        if ((iLongitude & 0x80000000) == 0x80000000) {
            iLongitude &= 0x7fffffff;
            realLon = iLongitude / 10000.0;
            realLon *= -1;
        } else {
            realLon = iLongitude / 10000.0;
        }

        return realLon;
    }

    public static float getHeading1() {
        if ((iHead1 & 0x8000) == 0x8000) {
            iHead1 &= 0x7fff;
            iHead1 *= -1;
        }
        float realHead = iHead1 / 100;
        return realHead;
    }

    public static float getHeading2() {
        if ((iHead2 & 0x8000) == 0x8000) {
            iHead2 &= 0x7fff;
            iHead2 *= -1;
        }
        float realHead = iHead2 / 100;
        return realHead;
    }

    public static float getBbLevel() {
        if ((iBBLevel & 0x8000) == 0x8000) {
            iBBLevel &= 0x7fff;
            iBBLevel *= -1;
        }
        float realValue = (iBBLevel / 100) / 5;
        return realValue;
    }

    public static float getBbVol() {
        if ((iBBVolume & 0x8000) == 0x8000) {
            iBBVolume &= 0x7fff;
            iBBVolume *= -1;
        }
        float realValue = iBBVolume / 100;
        return realValue;
    }

    public static float getTemp() {
        if ((iTemp & 0x8000) == 0x8000) {
            iTemp &= 0x7fff;
            iTemp *= -1;
        }
        float realValue = iTemp / 100;
        return realValue;
    }

    public static float getHumi() {
        if ((iHum & 0x8000) == 0x8000) {
            iHum &= 0x7fff;
            iHum *= -1;
        }
        float realValue = iHum / 100;
        return realValue;
    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return "0x" + result;
    }

}
