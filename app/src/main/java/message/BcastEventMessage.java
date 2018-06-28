package message;


import model.BcastModelMessage;

/**
 * Created by user on 24/04/2018.
 */

public class BcastEventMessage {
    private BcastModelMessage bcastMessageQueue;
    private String TAG = "smartwatch";
    public BcastEventMessage(BcastModelMessage bcastMessageQueue) {
        this.bcastMessageQueue = bcastMessageQueue;
    }
    public BcastModelMessage getMessageQueue() {
        return bcastMessageQueue;
    }

}
