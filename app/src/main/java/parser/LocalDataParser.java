package parser;

/**
 * Created by user on 20/04/2018.
 */

import android.util.Log;

import gps.GPSLocation;
import gps.GPSLogistik;

import java.text.DecimalFormat;

import services.LocalService;

public class LocalDataParser {
    private static final String LOG = "LocalDataParser-local";
    public static int iLongitude, iLatitude, iHead1, iHead2, iBBLevel,
            iBBVolume, iTemp, iHum, iTimestamp, tHelp, iComm, iGpsQuality,
            iGpsValid, iDTime, iAmCannon, iAmGun;
    private static double oldLat, oldLon;

    /*
     * SpvPos Local 02:62:00:80:80:43:80:27: 07:ff:07:
     * 00:00:07:0e:00:00:00:09:00:00:00:0a:00:00:00:08:00:00:00:0b:00:00:00:0c:00:
     * 00:00:0d:00:00:00:0f:00:10:00:11:00:12:00:13:00:14:00: 07:ff:08:
     * 01:00:03:00:04:00:05:00:06:00:00:00:00:00:00:00:00:00: 7a:60
     */
    public String parseSbcSentence(byte[] buffer) throws SecurityException {
        String locationSentence = null;

        double lat = 0, lon = 0;
        float head1 = 0, head2 = 0, bbLevel = 0, bbVol = 0, temp = 0, humi = 0;
        int pduLength = buffer[5] & 0x000000ff;
        int tHelp;
        int y = 0;
        int oid = 0;

        //Log.i(LOG, "Data length : " + pduLength);

		/*
		 * if (buffer[8] == (byte) 0x07) { // Log.i(LOG,"0x07 ada di... : "+x);
		 *
		 * }
		 */
        LocalService.cntString = 0;
        for (int x = 9; x < pduLength; x++) {

            oid = buffer[x] & 0x000000ff;
            tHelp = buffer[x + 1] & 0x000000ff;
            tHelp = tHelp << 8;
            oid |= tHelp;

            y = x + 1; // start of data OID

            switch (oid) {
                case 0xc101: // system status alarm mask
                    locationSentence = "System Status alarm mask";
                    //LocalService.extraIntent = "10";

                    LocalService.arrString[LocalService.cntString] = locationSentence;
                    LocalService.extraIntent[LocalService.cntString] = "10";

                    x = x + 4;
                    break;

                case 0xc100: // System Status
                    locationSentence = "System Status";
                    //LocalService.extraIntent = "9";
                    LocalService.arrString[LocalService.cntString] = locationSentence;
                    LocalService.extraIntent[LocalService.cntString] = "9";

                    x = x + 4;
                    break;

                case 0xc080: // frame resp
                    locationSentence = "Frame Resp";
//				LocalService.extraIntent = "11";
                    LocalService.arrString[LocalService.cntString] = locationSentence;
                    LocalService.extraIntent[LocalService.cntString] = "11";

                    x = x + 2;
                    break;

                case 0x07fe:
                    // link comm status (2)

                    iComm = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iComm |= tHelp;
                    iGpsQuality = (iComm >> 4) & 7;
                    iGpsValid = (iComm >> 3) & 1;

/*
				int bGps = buffer[++y];
				iComm = bGps & 0x1;
				iGpsQuality = (bGps >> 4) & 7;
				iGpsValid = (bGps >> 3) & 1;
				++y;
*/

                    // timestamp(4)
                    //Log.d(LOG, "Local: 0 T HELP 1 : "+buffer[y]);

                    iDTime = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;

                    Log.d(LOG, "Local: 1. " + iDTime );
                    iDTime |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 16;
                    Log.d(LOG, "Local: 2. " + iDTime);

                    iDTime |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 24;
                    iDTime |= tHelp;
                    Log.d(LOG, "Local: 4. " + iDTime);

                    // latitude (4)
                    iLatitude = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iLatitude |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 16;
                    iLatitude |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 24;
                    iLatitude |= tHelp;
                    lat = LocalDataParser.getLatitude();
                    Log.d(LOG, "Local: Latitude :~ " + lat);

                    // longitude (4)
                    iLongitude = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iLongitude |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 16;
                    iLongitude |= tHelp;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 24;
                    iLongitude |= tHelp;
                    lon = LocalDataParser.getLongitude();
                    Log.d(LOG, "Local: Longitude :~ " + lon);

                    iHead1 = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iHead1 |= tHelp;
                    head1 = LocalDataParser.getHeading1();
                    iHead2 = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iHead2 |= tHelp;
                    head2 = LocalDataParser.getHeading2();

                    if (LocalService.ismanloc) {
                        lat = LocalService.mlat;
                        lon = LocalService.mlon;
                    }

                    x = ++y;

                    if(lat<=90 && lat>=-90) oldLat=lat;
                    if(lon<=180 && lon>=-180) oldLon=lon;

                    locationSentence = "local;000," + 0xfff0 + "," + iDTime + "," + iDTime + ","
                            + new DecimalFormat("#0.000000").format(oldLat) + ","
                            + new DecimalFormat("#0.000000").format(oldLon) + ","
                            + head1 + "," + head2 + "," + iComm + "," + iGpsValid
                            + "," + iGpsQuality;

                    Log.d(LOG, "local: "+locationSentence);

                    GPSLocation GPS = new GPSLocation();
                    GPS.address = String.valueOf("0");
                    GPS.head1 = String.valueOf(head1);
                    GPS.head2= String.valueOf(head2);
                    GPS.icomm= String.valueOf(iComm);
                    GPS.iDTime= String.valueOf(iDTime);
                    GPS.iGpsQuality= String.valueOf(iGpsQuality);
                    GPS.iGpsValid= String.valueOf(iGpsValid);
                    GPS.lat= String.valueOf(new DecimalFormat("#0.000000").format(oldLat));
                    GPS.lon= String.valueOf(new DecimalFormat("#0.000000").format(oldLon));

                    Log.d(LOG, "GPS.address : "+ GPS.address);
                    Log.d(LOG, "GPS.head1 : "+ GPS.head1);
                    Log.d(LOG, "GPS.head2 : "+ GPS.head2);
                    Log.d(LOG, "GPS.icomm : "+ GPS.icomm);
                    Log.d(LOG, "GPS.iDTime : "+ GPS.iDTime);
                    Log.d(LOG, "GPS.iGpsQuality : "+ GPS.iGpsQuality);
                    Log.d(LOG, "GPS.iGpsValid : "+ GPS.iGpsValid);
                    Log.d(LOG, "GPS.lat : "+ GPS.lat);
                    Log.d(LOG, "GPS.lon : "+ GPS.lon);
                    //MainActivity.UIQueue.add(Pair.create("GPSLOCATION",(Object) GPS));

                    break;
                case 0x07ff:
                    locationSentence = "No OID";
                    break;

                case 0x08ff:
                    // Log.d(LOG, "parsing SPV");
                    // BB Level
                    iBBLevel = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iBBLevel |= tHelp;
                    float fBBLevel = (float) iBBLevel;
                    //fBBLevel = fBBLevel / 100;

                    // AM Kanon
                    iAmCannon = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iAmCannon |= tHelp;

                    // AM Gun
                    iAmGun = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iAmGun |= tHelp;

                    // Temp
                    iTemp = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iTemp |= tHelp;
                    float fTemp = iTemp;
                    fTemp = fTemp / 100;

                    // hum
                    iHum = buffer[++y] & 0x000000ff;
                    tHelp = buffer[++y] & 0x000000ff;
                    tHelp = tHelp << 8;
                    iHum |= tHelp;
                    int fHum = iHum;
                    //fHum = fHum / 100;

                    locationSentence = 0xfff0 + "," + "s" + "," + fBBLevel + ","
                            + iAmCannon + "," + iAmGun + "," + fTemp + "," + fHum;
                    // Log.d(LOG, "Local: "+locationSentence);
                    //LocalService.extraIntent = "1";

                    GPSLogistik GPS2 = new GPSLogistik();
                    GPS2.address = String.valueOf("0");
                    GPS2.fBBLevel = String.valueOf(fBBLevel);
                    GPS2.fHum= String.valueOf(fHum);
                    GPS2.fTemp= String.valueOf(fTemp);
                    GPS2.iAmCannon= String.valueOf(iAmCannon);
                    GPS2.iAmGun= String.valueOf(iAmGun);

                    //MainActivity.UIQueue.add(Pair.create("GPSLOGISTIK",(Object) GPS2));

                    //LocalService.arrString[LocalService.cntString] = locationSentence;
                    //LocalService.extraIntent[LocalService.cntString] = "1";

                    x = ++y;
                    break;

                default:
                    locationSentence = "No OID";
                    //LocalService.extraIntent = "100";
                    LocalService.arrString[LocalService.cntString] = locationSentence;
                    LocalService.extraIntent[LocalService.cntString] = "100";

                    return locationSentence;

            }
            LocalService.cntString++;
        }
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

}
