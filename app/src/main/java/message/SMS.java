package message;

import java.util.Date;

import model.ModelMessage;
import model.ModelSMS;

public class SMS {

    public SMS(String msg){
        //packetString driverview:local;019,0~38~0~1~5b922287~testsms~38
        String[] arrMsg =  msg.split("~",-1);
        int senderId = Integer.parseInt(arrMsg[1]);
        String textMsg = arrMsg[5];
        ModelSMS modelSMS = new ModelSMS(senderId, new Date(), textMsg);


    }



}
