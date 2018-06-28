package message;


import model.NeighbourModelMessage;

/**
 * Created by user on 24/04/2018.
 */

public class NeighbourEventMessage {
    private NeighbourModelMessage neighbourMessageQueue;
    private String TAG = "smartwatch";
    public NeighbourEventMessage(NeighbourModelMessage neighbourMessageQueue) {
        this.neighbourMessageQueue = neighbourMessageQueue;
    }
    public NeighbourModelMessage getMessageQueue() {
        return neighbourMessageQueue;
    }

}
