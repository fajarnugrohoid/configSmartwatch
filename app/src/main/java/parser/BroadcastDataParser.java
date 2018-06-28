package parser;

import android.util.Log;

import gps.GPSLocation;
import gps.GPSLogistik;

import java.text.DecimalFormat;

/**
 * Created by user on 17/05/2018.
 */

public class BroadcastDataParser {
    private static final String LOG = "parser-bcast";
    public static int iLongitude, iLatitude, iHead1, iHead2, iBBLevel,
            iBBVolume, iTemp, iHum, iTimestamp, tHelp, iComm, iGpsQuality,
            iGpsValid, iDTime, iAmCannon, iAmGun,t,
            iSuhu,iHumi,iJarak,iCurah,iAngin, iData, iSubtype, iCntFileLength,
            iSubCodeLSB, iSubCodeMSB, iFileName;
    public static long iAddSrc,iFuture ,SmsDest;
    public static int iCode,iSubcode,iPage , iCount , iObjectId;
    public int waktu;
    public static double arlat[] = new double[120];
    public static double arlon[] = new double[120];
    private static byte[] bTime = new byte[4];

    private static double oldLat, oldLon;


    public String parseSbcSentence(byte[] buffer) throws SecurityException {

        String locationSentence="bcast;";
        double lat = 0, lon = 0;
        float head1 = 0, head2 = 0;
        int pduLength;
        int tHelp;
        int y = 0;
        int oid = 0;
        long address = 0 , address2 =0;
        int cnt = 0;
        int master = 0;
        int base = 0;
        int data=0;
        int status_active = 0;
        GPSLocation gpsstatus2 = new GPSLocation();

        pduLength = buffer[5] & 0x000000ff;
        tHelp = buffer[6] & 0x0000007f;
        tHelp = tHelp << 8;
        pduLength |= tHelp;
        //Log.i(LOG, "Data length : " + pduLength);

        int xdata = 0;
        for (int x = 8; x < pduLength; x++) { // address for loop
            Log.i(LOG, "pduLength : " + pduLength);

            //address 4 byte
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
            Log.i(LOG, "Data Address : " + address);

            cnt = buffer[++x] & 0x000000ff; // 10
            tHelp = buffer[++x] & 0x000000ff;
            tHelp = tHelp << 8; // 11
            cnt |= tHelp;
            //Log.i(LOG, "Data cnt : " + cnt);

            master = buffer[++x] & 0x000000ff; // 10
            //Log.i(LOG, "Data master : " + master);

            //++x; // 12
            base = x;
            int tescount = 0;
            for (int z = 0; z < cnt; z++) { // oid for loop
                //		Log.d(LOG,"tescount: "+tescount++);
                //		Log.d(LOG,"z: "+z+"  count:"+cnt);
                oid = buffer[++z + base] & 0x000000ff; // 13
                tHelp = buffer[++z + base] & 0x000000ff;
                tHelp = tHelp << 8; // 14
                oid |= tHelp;
                Log.d(LOG,"oid: "+oid);
                switch (oid) {
                    case 0xc102: // way point
                        break;
                    case 0xc101: // system status alarm mask
                        locationSentence += address + ",System Status alarm mask";
                        Log.d(LOG, "0xc101 : -->" + locationSentence);
                        z += 4;
                        y = z ;
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
                        Log.d(LOG, "0xc080:" + locationSentence);
                        z += 2;
                        y = z;
                        break;


                    case 0xc0ff: // SMS
                        Log.i(LOG,"Entering sms parsing");
                        //src,sentTime,lenRoomChat,sRoomChat,lenMessage,sMessage (perubahan sms)
                        int cntSMS = buffer[++z+base] & 0x000000ff; //15

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

                        Log.i(LOG, "Data Address : " + iAddSrc);

                        SmsDest = 0;
                        Log.i(LOG, "Data Destination : " + SmsDest);
                        //*/
                        cntSMS-=2;

                        bTime[0] = buffer[++z+base];
                        bTime[1] = buffer[++z+base];
                        bTime[2] = buffer[++z+base];
                        bTime[3] = buffer[++z+base];

                        //waktu =ByteBuffer.wrap(bTime).getInt();
                        int codeSMS = buffer[++z + base] & 0x000000ff;
                        Log.d(LOG, "ignore sms report from Bcast = " + String.valueOf(codeSMS));

                        if (codeSMS == 225) {
                            Log.d(LOG, "ignore sms report from Bcast");
                            int iGroup = buffer[++z + base] & 0x000000ff; // 17

                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iGroup |= tHelp;


                            int iCntMessage = buffer[++z + base] & 0x000000ff;

                            String byteInputMessage;
                            StringBuilder sbMessage = new StringBuilder();
                            String STimeOrg = null;
                            byte[] STimeOrg2 = new byte[4];

                            STimeOrg2[0] = buffer[++z+base];
                            STimeOrg2[1] = buffer[++z+base];
                            STimeOrg2[2] = buffer[++z+base];
                            STimeOrg2[3] = buffer[++z+base];
                            try {
                                STimeOrg =getHexString(bTime);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            locationSentence += iAddSrc+"~"+address+"~"+iGroup+"~"+STimeOrg+
                                    "~"+STimeOrg2;
                            Log.v("Data Parser" , "DATA SMS DARI Broadcast : "+locationSentence);
                            //locationSentence = "ignore sms report from Neigh";

                            //* queue
                            /*
                            SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(iAddSrc);
                            sms.iAddSrc = String.valueOf(address);
                            sms.iGroup = String.valueOf(iGroup);
                            sms.STimeOrg = String.valueOf(STimeOrg);
                            sms.sbMessage = sbMessage.toString(); */


                            //MainActivity.UIQueue.add(Pair.create("SMSACK",(Object) sms));
                            //return locationSentence;
                            ++z;++z;++z;++z;
                            ++z;
                        } else {

                            int iGroup = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iGroup |= tHelp;

                            int iCntMessage = buffer[++z + base] & 0x000000ff;

                            String byteInputMessage;
                            StringBuilder sbMessage = new StringBuilder();

                            for (int j=0; j<iCntMessage;j++) {
                                sbMessage.append((char) buffer[++z+base]);

                            }

                            String STimeOrg = null;
                            try {
                                STimeOrg =getHexString(bTime);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            locationSentence += address+"~"+iAddSrc+"~"+iGroup+"~"+STimeOrg+
                                    "~"+sbMessage.toString();
                            Log.d(LOG, "else address iAddSrc:" + locationSentence);
                            //* queue
                            /*SmsStatus sms = new SmsStatus();
                            sms.address = String.valueOf(address);
                            sms.iAddSrc = String.valueOf(iAddSrc);
                            sms.iGroup = String.valueOf(iGroup);
                            sms.STimeOrg = String.valueOf(STimeOrg);
                            sms.sbMessage = sbMessage.toString();
                            MainActivity.UIQueue.add(Pair.create("SMS",(Object) sms)); */
                            y=z;
                        }
                        break;

                    case 0xc103: // LSTRING
                        //Log.i(LOG, "Entering lstring parsing");

                        int cntLstring = buffer[++z + base] & 0x000000ff;; // 15
                        tHelp = buffer[++z+base] & 0x000000ff; //18
                        tHelp = tHelp << 8;
                        cntLstring |= tHelp;
                        Log.d(LOG, "cnt  : "+cntLstring);

                        t=z;
                        ++z;// cursor to LString code
                        int code;
                        code = buffer[z + base] & 0x000000ff;
                        Log.d(LOG, "code  : "+code);

                        //update		accept obj		 accept lock   accept destroy reject/delete obj
                        // situasi
                        if ( (code == 1) || (code == 9) || (code == 25) || (code == 41) ||
                                (code == 13) ||(code == 29) ||(code == 45)  || (code == 73)|| (code == 77)||(code==193)||(code==145)||(code==209)||(code==161)||(code==5)||
                                (code == 21)||(code == 37)||(code == 81)||(code == 97)||(code == 54)) {

                            // Status(1)
                            int iStatus = 0; // 17
                            switch (code) {
                                case 1 	: iStatus = 0; break;//update
                                case 13	: iStatus = 3; break;//delete/reject
                                case 29	: iStatus = 11; break;//delete/rejectLock
                                case 45	: iStatus = 12; break;//delete/deleteLock
                                //case 9	: iStatus = 2; break;//obj is accepted
                                case 25 : iStatus = 6; break;//lock is accepted
                                case 41 : iStatus = 7; break; //hcr is accepted
                                case 73 : iStatus = 9; break; //reg is accepted
                                case 77 : iStatus = 10; break; //reg is reject

                                //baru
                                case 9: iStatus = 0; break;//update
                                case 193: iStatus = 3; break;//delete
                                case 145: iStatus = 6; break;//lock accepted
                                case 161: iStatus = 7; break;//hcr accepted
                                case 209: iStatus = 11; break;//lock rejected
                                //case 69: iStatus = 9; break;//register neigh

                                //oid baru
                                case 54: iStatus = 14; break;//delete all situasi

                                case 5: iStatus = 0; break;//update
                                case 21: iStatus = 6; break;//lock from neigh
                                case 81: iStatus = 6; break;//lock from neigh
                                case 37: iStatus = 7; break;//hcr from neigh


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
                            cntLstring -= 6;
                            //Log.d(LOG, "iSymId  : "+iSymId);


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

                            cntLstring -= 4;

                            // MasterId(2)//object id
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
                                byteInputLabel = String.format("%c", buffer[++z
                                        + base]);
                                sbLabel.append(byteInputLabel.trim());

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
                            lat = (this.getLatitudeDD()/100);
                            lon = (this.getLongitudeDD()/100);

                            locationSentence += address + "," + lat + "," + lon
                                    + "," + iSymId + "," + iSlaveId + ","
                                    + iMasterId + "," + sbLabel.toString() + ","
                                    + iStatus + "," + sbDesc.toString() + "," + iAddSrc;
                            Log.d(LOG, "0xc103 address, lat:" + locationSentence);
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
                            poi.iAddSrc = String.valueOf(iAddSrc);
                            //*/

                            Log.d("Data Parser " ,"Location Sentece : " +  locationSentence);
                            //MainActivity.UIQueue.add(Pair.create("POI",(Object) poi));


                        }else if ( (code == 2) || (code == 10) || (code == 14)||(code == 130)||(code == 194)||(code == 6)||(code == 55)) {
                            Log.d(LOG, "CODE : "+code);
                            int iStatus = 0; // 17	//update
                            if(code == 14) iStatus = 3;	//delete
                            else if(code == 10) iStatus = 2;	// accept

                            else if	(code ==130)iStatus=0;
                            else if	(code ==6)iStatus=0;
                            else if	(code ==194)iStatus=3;
                            else if	(code ==55)iStatus=15;//delete all skenario



                            iAddSrc = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iAddSrc |= tHelp;

                            Log.d(LOG, "address1  : "+address);

                            Log.d(LOG, "address  : "+iAddSrc);


                            iFuture = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iFuture |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iFuture |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iFuture |= tHelp;
                            Log.d(LOG, "iFuture  : "+iFuture);

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

                            Log.d(LOG, "iSlaveId  : "+iSlaveId);

                            // MasterId(2)
                            int iMasterId = buffer[++z + base] & 0x000000ff; // 17
                            tHelp = buffer[++z + base] & 0x000000ff; // 18
                            tHelp = tHelp << 8;
                            iMasterId |= tHelp;


                            //Add 2204


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

                                arlat[i] = (this.getLatitudeDD())/100;
                                arlon[i] = (this.getLongitudeDD())/100;

                            }



                            // Label Length(1)
                            int iCntLabel = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputLabel;
                            StringBuilder sbLabel = new StringBuilder();

                            for (int j = 0; j < iCntLabel; j++) {
                                sbLabel.append((char)buffer[++z+ base]);

                            }

                            int iCntDesc = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputDesc;
                            StringBuilder sbDesc = new StringBuilder();

                            for (int j = 0; j < iCntDesc; j++) {
                                sbDesc.append((char)buffer[++z+ base]);

                            }

                            locationSentence ="";
                            //* queue
                            /*
                            SkenarioStatus skenario = new SkenarioStatus();
                            skenario.Address = String.valueOf(address);
                            skenario.Slaveid = String.valueOf(iSlaveId);
                            skenario.Masterid = String.valueOf(iMasterId);
                            skenario.Label = sbLabel.toString();
                            skenario.Status = String.valueOf(iStatus);
                            skenario.Description = sbDesc.toString();
                            skenario.NoPoint = String.valueOf(iNoPoint);
                            //*/
                            for (int i = 0; i < iNoPoint; i++) {

                                locationSentence += locationSentence + ","
                                        + arlat[i] + "," + arlon[i];
                                Log.d(LOG, "arlat[i], arlon[i]:" + locationSentence);
                                //skenario.gPoints.add(GeoPoint.from2DoubleString(String.valueOf(arlat[i]), String.valueOf(arlon[i])));
                            }
                            /*skenario.gPoints = locationSentence;

                            MainActivity.UIQueue.add(Pair.create("Skenario",(Object) skenario)); */

                            y=z;

                        }else if ( code == 0x80) {
                            int iStatus = 0x80; //

                            iAddSrc = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iAddSrc |= tHelp;
                            Log.d(LOG, "iAddSrc  : "+iAddSrc);


                            iSuhu = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iSuhu |= tHelp;
                            Log.d(LOG, "iSuhu  : "+iSuhu);

                            iHumi = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iHumi |= tHelp;

                            iJarak = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iJarak |= tHelp;

                            iCurah = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iCurah |= tHelp;

                            iAngin = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAngin |= tHelp;

                            locationSentence += address + "," + iSuhu + "," + iHumi
                                    + "," + iJarak + "," + iCurah + ","
                                    + iAngin + ","
                                    + iStatus ;

                            Log.d(LOG, "Data Cuaca  : "+locationSentence);

                            /*
                            CuacaStatus cuaca = new CuacaStatus();
                            cuaca.Address = String.valueOf(address);
                            cuaca.Suhu = String.valueOf(iSuhu);
                            cuaca.Humidity = String.valueOf(iHumi);
                            cuaca.Jarak = String.valueOf(iJarak);
                            cuaca.Curah_Hujan = String.valueOf(iCurah);
                            cuaca.Angin = String.valueOf(iAngin);
                            cuaca.Status =String.valueOf(iStatus);

                            MainActivity.UIQueue.add(Pair.create("CUACA",(Object) cuaca)); */



                        }

                        else if ( code == 0x81 ) {

                            int iStatus = 0x81; //

                            iAddSrc = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iAddSrc |= tHelp;


                            iData = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iData |= tHelp;

                            iSubtype = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iSubtype |= tHelp;

                            locationSentence += iAddSrc + "," + iData + "," + iSubtype +","+address;
                            Log.d(LOG, "iAddSrc, iData:" + locationSentence);

                        }else if ( code == 0x82 ) {
                            //FTP
                            int iStatus = 0x82; //
                            iAddSrc = buffer[++z+base] & 0x000000ff; //17
                            tHelp = buffer[++z+base] & 0x000000ff; //18
                            tHelp = tHelp << 8;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 16;
                            iAddSrc |= tHelp;
                            tHelp = buffer[++z + base] & 0x000000ff;
                            tHelp = tHelp << 24;
                            iAddSrc |= tHelp;


                            iSubCodeLSB = buffer[++z+base] & 0x000000ff; //17
                            iSubCodeMSB = buffer[++z+base] & 0x000000ff; //18
                            if(iAddSrc==65535){
                                z+=2;
                            }else{
                            }
                            //z+=2;
                            iCntFileLength = buffer[++z+base]& 0x000000ff; //17

                            String byteInputLabel;
                            StringBuilder sbLabel = new StringBuilder();

                            for (int j = 0; j < iCntFileLength; j++) {
                                byteInputLabel = String.format("%c", buffer[++z + base]);
                                sbLabel.append(byteInputLabel.trim());
                            }

                            locationSentence += iAddSrc + "," + iSubCodeLSB + "," + iSubCodeMSB + "," + sbLabel.toString();//sbLabel;

                            Log.i("sentence", "sentence : " +locationSentence);


                        }else if (buffer[z + base] == 222) {

                            // SlaveId(2)
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

                            // Status(1)
                            int iStatus = buffer[++z + base] & 0x000000ff; // 17

                            // Label Length(1)
                            int iCntLabel = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputLabel;
                            StringBuilder sbLabel = new StringBuilder();

                            for (int j = 0; j < iCntLabel; j++) {
                                byteInputLabel = String.format("%c", buffer[++z
                                        + base]);
                                sbLabel.append(byteInputLabel.trim());

                            }

                            int iCntDesc = buffer[++z + base] & 0x000000ff; // 17

                            String byteInputDesc;
                            StringBuilder sbDesc = new StringBuilder();

                            for (int j = 0; j < iCntDesc; j++) {
                                byteInputDesc = String.format("%c", buffer[++z
                                        + base]);
                                sbDesc.append(byteInputDesc.trim());

                            }

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

                                arlat[i] = this.getLatitudeDD();
                                arlon[i] = this.getLongitudeDD();
                            }

                            // AddressDestination,IsMaster,SlaveID,MasterID,Label,Status,Description,NoPoints,Points[NoPoints]

                            locationSentence += address + "," + iSlaveId + ","
                                    + iMasterId + "," + sbLabel.toString() + ","
                                    + iStatus + "," + sbDesc.toString() + ","
                                    + iNoPoint;
                            Log.d(LOG, "address,iSlaveId:" + locationSentence);

                            for (int i = 0; i < iNoPoint; i++) {

                                locationSentence += locationSentence + ","
                                        + arlat[i] + "," + arlon[i];
                                Log.d(LOG, "arlat[i],arlon[i]:" + locationSentence);
                            }
                            z = Math.abs(t + cntLstring);
                        }

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
                        Log.d(LOG, "iLatitude:" + iLatitude);
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
                        Log.d(LOG, "iLongitude:" + iLongitude);
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

                        Log.d(LOG, "oldLat:" + oldLat + "===lat:" + lat);
                        Log.d(LOG, "oldLon:" + oldLon + "===lat:" + lon);

                        locationSentence += "000,"+address + "," + "s" + "," + iDTime + ","
                                + new DecimalFormat("#0.000000").format(oldLat) + "," + new DecimalFormat("#0.000000").format(oldLon) + "," + head1 + "," + head2 + ","
                                + iComm + "," + iGpsValid + "," + iGpsQuality;
                        Log.d("MainActivity" , "address : "+address);
                        Log.d("MainActivity" , "Location : "+locationSentence);
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

                        //MainActivity.UIQueue.add(Pair.create("GPSLOCATION",(Object) gpsstatus));


                        y=z;

                        break;
                    case 0x08ff:
                        Log.d(LOG, "parsing SPV");
                        // BB Level
                        iBBLevel = buffer[++z + base] & 0x000000ff;

                        tHelp = buffer[++z + base] & 0x000000ff;

                        tHelp = tHelp << 8;
                        iBBLevel |= tHelp;
                        float fBBLevel = iBBLevel;
                        //fBBLevel = fBBLevel / 10;

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

                        Log.d("MainActivity" , "Logistik : "+locationSentence);

                        //* queue
                        GPSLogistik logistikstatus = new GPSLogistik();
                        logistikstatus.address = String.valueOf(address);
                        logistikstatus.s = "s";
                        logistikstatus.fBBLevel = String.valueOf(fBBLevel);
                        logistikstatus.iAmCannon = String.valueOf(iAmCannon);
                        logistikstatus.iAmGun = String.valueOf(iAmGun);
                        logistikstatus.fTemp = String.valueOf(fTemp);
                        logistikstatus.fHum = String.valueOf(fHum);

                        /*
                        MainActivity.UIQueue.add(Pair.create("GPSLOGISTIK",(Object) logistikstatus));
                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z;
                        ++z;++z;++z;++z; */


                        y=z;
                        break;

                    default :
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
        Log.d(LOG, "getLatitude-BCast:" + iLatitude);
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
        Log.d(LOG, "getLongitude-BCast:" + iLongitude);
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

    //
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
