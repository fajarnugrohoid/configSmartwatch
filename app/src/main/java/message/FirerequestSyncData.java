package message;

import android.location.Location;

import java.util.Date;

/**
 * Created by user on 19/04/2018.
 */

public class FirerequestSyncData extends TCFMessage {
    @Override
    public TCFMessageType getMessageType() {
        return TCFMessageType.SyncData;
    }
    public FireRequestCommand Command = FireRequestCommand.Send;
    public int Sender = 3;
    public Date SentTime = new Date();
    public int CorrectionNo = 1;
    public Location ObserverPosition = new Location("");
    //public  Location TargetPosition = new Location("");
    public  double Arah = 0;
    public  double Jarak = 0;
    public  int AmmoShot = 1;
}
