package parser;

import android.util.Log;

import java.text.DecimalFormat;

import gps.GPSLocation;
import gps.GPSLogistik;

/**
 * Created by user on 21/05/2018.
 */

public class NeighbourDataParser {
    private static final String LOG = "smartwatch-neigh";
    public static int iLongitude, iLatitude, iHead1, iHead2, iBBLevel,
            iBBVolume, iTemp, iHum, iTimestamp, tHelp, iComm, iGpsQuality,
            iGpsValid, iDTime, iAmCannon, iAmGun,  t ;
    public static long iAddSrc , iFuture , SmsDest  ;
    private String strOid;
    private static double oldLat, oldLon;


    public static double arlat[] = new double[120];
    public static double arlon[] = new double[120];
    private static byte[] bTime = new byte[4];



    public String parseSbcSentence(byte[] buffer) throws SecurityException {

        String locationSentence = "neighbour;";
        double lat = 0, lon = 0;
        float head1 = 0, head2 = 0, bbLevel = 0, bbVol = 0, temp = 0, humi = 0;
        int pduLength;
        int tHelp;
        int y = 0;
        int oid = 0;
        int address = 0;
        int master = 0;
        int cnt = 0;
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
            Log.i(LOG, "Data address : " + address);

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
                Log.i(LOG, "Data oid : " + oid);

                switch (oid) {

                    case 0xc102: // way point

                        break;

                    case 0xc101: // system status alarm mask
                        locationSentence += address + ",System Status alarm mask";
                        z += 4;
                        y = z;
                        break;

                    case 0xc100: // System Status
                        status_active = buffer[++z + base] & 0x000000ff;
                        Log.d(LOG, "StatusActive : " + String.valueOf(status_active));
                        locationSentence += address + ",System Status,"+status_active;

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
                        // Log.i(LOG,"Entering sms parsing");
                        // src,sentTime,lenRoomChat,sRoomChat,lenMessage,sMessage
                        // (perubahan sms)
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
                            locationSentence += iAddSrc+"~"+address+"~"+iGroup+"~"+STimeOrg+
                                    "~"+sbMessage.toString();

                            //* queue
                            /*SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(iAddSrc);
                            sms.iAddSrc = String.valueOf(address);
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
                                //byteInputMessage = String.format("%c", buffer[++z
                                //		+ base]);
                                //sbMessage.append(byteInputMessage);
                                sbMessage.append((char)buffer[++z+ base]);

                            }

                            String STimeOrg = null;
                            try {
                                STimeOrg =getHexString(bTime);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            //dst,src,sRoomChat,sentTime,sMessage (perubahan sms)
                            locationSentence += address+"~"+iAddSrc+"~"+iGroup+"~"+STimeOrg+
                                    "~"+sbMessage.toString();

                            //* queue
                            /*SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(address);
                            sms.iAddSrc = String.valueOf(iAddSrc);
                            sms.iGroup = String.valueOf(iGroup);
                            sms.STimeOrg = String.valueOf(STimeOrg);
                            sms.sbMessage = sbMessage.toString(); */

                            //MainActivity.UIQueue.add(Pair.create("SMS",(Object) sms));

                        }
                        z = t;
                        y = z;

                        break;

                    case 0xc103: // LSTRING

                        int cntLstring = buffer[++z + base] & 0x000000ff;; // 15
                        tHelp = buffer[++z+base] & 0x000000ff; //18
                        tHelp = tHelp << 8;
                        cntLstring |= tHelp;

                        t = z;
                        ++z;// cursor to LString code
                        byte code;
                        code = buffer[z + base];
                        Log.i(LOG, "Lstring code : " + code);
                        if ((code == 5) || (code == 21) || (code == 37) || (code == 69)) {
                            //request confirm new object (5) or request confirm lock (21) request confirm hcr (37) || req regis(69)
                            // Status(1)
                            int iStatus = 1; // 17

                            switch(code) {

                                case 5:
                                    iStatus = 1;
                                    break;
                                case 21:
                                    iStatus = 4;
                                    break;
                                case 37:
                                    iStatus = 5;
                                    break;
                                case 69:
                                    iStatus = 8;//req registrasi
                                    break;
                            }

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

                            Log.d(LOG, "address  : "+iAddSrc);

                            iFuture = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iFuture |= tHelp;
                            tHelp = buffer[++z+base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iFuture |= tHelp;
                            tHelp = buffer[++z+base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iFuture |= tHelp;

                            Log.d(LOG, "iFuture  : "+iFuture);

                            // SymId(2) /picture id
                            int iSymId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iSymId |= tHelp;
                            cntLstring -= 2;

                            // SlaveId(2) //address
                            long iSlaveId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iSlaveId |= tHelp;
                            tHelp = buffer[++z+base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iSlaveId |= tHelp;
                            tHelp = buffer[++z+base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iSlaveId |= tHelp;
                            Log.d(LOG, "iSlaveId  : "+iSlaveId);
                            cntLstring -= 2;

                            // MasterId(2)
                            int iMasterId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iMasterId |= tHelp;
                            cntLstring -= 2;
                            Log.d(LOG, "iMasterId  : "+iMasterId);


                            iLatitude = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iLatitude |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iLatitude |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iLatitude |= tHelp;
                            cntLstring -= 4;

                            // lon(4)
                            iLongitude = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iLongitude |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iLongitude |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iLongitude |= tHelp;
                            cntLstring -= 4;

                            // Label Length(1)
                            int iCntLabel = buffer[++z + base] & 0x000000ff; // 17
                            cntLstring -= 1;
                            String byteInputLabel;
                            StringBuilder sbLabel = new StringBuilder();

                            for (int j = 0; j < iCntLabel; j++) {
                                sbLabel.append((char)buffer[++z+ base]);

                            }

                            cntLstring -= iCntLabel;

                            int iCntDesc = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputDesc;
                            StringBuilder sbDesc = new StringBuilder();

                            for (int j = 0; j < iCntDesc; j++) {
                                byteInputDesc = String.format("%c", buffer[++z
                                        + base]);
                                sbDesc.append(byteInputDesc.trim());

                            }
                            lat = (this.getLatitudeDD())/100;
                            lon = (this.getLongitudeDD()/100);

						/*locationSentence = iAddSrc + "," + lat + "," + lon
								+ "," + iSymId + "," + iSlaveId + ","
								+ iMasterId + "," + sbLabel.toString() + ","
								+ iStatus + "," + sbDesc.toString();*/

                            locationSentence += address + "," + lat + "," + lon
                                    + "," + iSymId + "," + iSlaveId + ","
                                    + iMasterId + "," + sbLabel.toString() + ","
                                    + iStatus + "," + sbDesc.toString() + "," + iAddSrc;

                            //LocalService.arrString[LocalService.cntString] = locationSentence;

                            //* queue
                            /*
                            PoiStatus poi = new PoiStatus();
                            poi.Address = String.valueOf(address);
                            poi.lat = String.valueOf(lat);
                            poi.lon = String.valueOf(lon);
                            poi.iSymId = String.valueOf(iSymId);
                            poi.iSlaveId = String.valueOf(iSlaveId);
                            poi.iMasterId = String.valueOf(iMasterId);
                            poi.sbLabel = sbLabel.toString();
                            poi.iStatus = String.valueOf(iStatus);
                            poi.sbDesc = sbDesc.toString();
                            poi.iAddSrc = String.valueOf(iAddSrc); */
                            //MainActivity.UIQueue.add(Pair.create("POI",(Object) poi));


                        }

                        else if ((code == 6)) {
                            //request confirm new scenario
                            // Log.d(LOG, "Entering lstring '4'");
                            // SlaveId(2)
                            int iStatus = 1; // 17

                            iAddSrc = buffer[++z + base] & 0x000000ff; //17
                            tHelp = buffer[++z + base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iAddSrc |= tHelp;


                            iFuture = buffer[++z + base] & 0x000000ff; //17
                            tHelp = buffer[++z + base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iFuture |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iFuture |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iFuture |= tHelp;


                            int iObjectId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iObjectId |= tHelp;

                            long iSlaveId = buffer[++z + base] & 0x000000ff;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 8;
                            iSlaveId |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iSlaveId |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iSlaveId |= tHelp;


                            // MasterId(2)
                            int iMasterId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iMasterId |= tHelp;


                            int iNoPoint = buffer[++z + base];

                            for (int i = 0; i < iNoPoint; i++) {
                                iLatitude = buffer[++z + base] & 0x000000ff; // 17
                                tHelp = buffer[++z + base] & 0x000000ff; // 18
                                tHelp = tHelp << 8;
                                iLatitude |= tHelp;
                                tHelp = buffer[++z + base] & 0x000000ff;
                                tHelp = tHelp << 16;
                                iLatitude |= tHelp;
                                tHelp = buffer[++z + base] & 0x000000ff;
                                tHelp = tHelp << 24;
                                iLatitude |= tHelp;

                                // lon(4)
                                iLongitude = buffer[++z + base] & 0x000000ff; // 17
                                tHelp = buffer[++z + base] & 0x000000ff; // 18
                                tHelp = tHelp << 8;
                                iLongitude |= tHelp;
                                tHelp = buffer[++z + base] & 0x000000ff;
                                tHelp = tHelp << 16;
                                iLongitude |= tHelp;
                                tHelp = buffer[++z + base] & 0x000000ff;
                                tHelp = tHelp << 24;
                                iLongitude |= tHelp;

                                if (i == (iNoPoint)) {

                                } else {
                                    arlat[i] = (this.getLatitudeDD()) / 100;
                                    arlon[i] = (this.getLongitudeDD() / 100);
                                }
                            }

                            // Label Length(1)
                            int iCntLabel = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputLabel;
                            StringBuilder sbLabel = new StringBuilder();

                            for (int j = 0; j < iCntLabel; j++) {

                                sbLabel.append((char) buffer[++z + base]);
                            }

                            int iCntDesc = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputDesc;
                            StringBuilder sbDesc = new StringBuilder();

                            for (int j = 0; j < iCntDesc; j++) {

                                sbDesc.append((char) buffer[++z + base]);
                            }


                            locationSentence += iSlaveId + "," + iMasterId + ","
                                    + sbLabel.toString() + ","
                                    + iStatus + "," + sbDesc.toString() + ","
                                    + iNoPoint;


                            String masukanfreedraw = "";
                            for (int i = 0; i < iNoPoint; i++) {
                                locationSentence += locationSentence + ","
                                        + arlat[i] + "," + arlon[i];
                                masukanfreedraw = masukanfreedraw + ","
                                        + arlat[i] + "," + arlon[i];
                            }
                            Log.d(LOG, "iAddSrc = " + iAddSrc);
                            Log.d(LOG, "iFuture = " + iFuture);
                            Log.d(LOG, "address = " + address);

                            //LocalService.arrString[LocalService.cntString] = locationSentence;
                            locationSentence = "";
                            //* queue
                            /*SkenarioStatus skenario = new SkenarioStatus();
                            skenario.Address = String.valueOf(address);
                            skenario.Slaveid = String.valueOf(iSlaveId);
                            skenario.Masterid = String.valueOf(iMasterId);
                            skenario.Label = sbLabel.toString();
                            skenario.Status = String.valueOf(iStatus);
                            skenario.Description = sbDesc.toString();
                            skenario.NoPoint = String.valueOf(iNoPoint);
                            skenario.gPoints = String.valueOf(masukanfreedraw);*/

                            //MainActivity.UIQueue.add(Pair.create("SKENARIO", (Object) skenario));


                            z = t + cntLstring;
                        }
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
                        //gpsstatus2 = gpsstatus;
                        //MainActivity.UIQueue.add(Pair.create("GPSLOCATION",(Object) gpsstatus));

                        y = z;
                        break;
                    case 0x08ff:
                        // BB Level
                        iBBLevel = buffer[++z + base] & 0x000000ff;

                        tHelp = buffer[++z + base] & 0x000000ff;

                        tHelp = tHelp << 8;
                        iBBLevel |= tHelp;
                        float fBBLevel = iBBLevel;
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
                        logistikstatus.iAmCannon = String.valueOf(iAmCannon);
                        logistikstatus.iAmGun = String.valueOf(iAmGun);
                        logistikstatus.fTemp = String.valueOf(fTemp);
                        logistikstatus.fHum = String.valueOf(fHum);

                        //MainActivity.UIQueue.add(Pair.create("GPSLOGISTIK",(Object) logistikstatus));

                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z;

					/*
					// Engine system struct
					++z;
					++z;
					++z;
					++z;

					// Door Window struct
					++z;
					++z;
					++z;
					++z;
					*/
                        y = z;
                        break;

                    default:
                        locationSentence = "No OID";
                        return locationSentence;

                } // end switch
                Log.d(LOG, "neighbour: " + locationSentence);

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
        Log.d(LOG, "getLatitude-Neighbour:" + iLatitude);
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
        Log.d(LOG, "getLongitude-Neighbour:" + iLongitude);
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
        return result;
    }
}
