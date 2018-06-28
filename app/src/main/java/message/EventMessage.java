package message;


import model.ModelMessage;

/**
 * Created by user on 24/04/2018.
 */

public class EventMessage {
    private ModelMessage messageQueue;
    private String TAG = "smartwatch";
    public EventMessage(ModelMessage messageQueue) {
        this.messageQueue = messageQueue;
    }
    public ModelMessage getMessageQueue() {
        return messageQueue;
    }

}
