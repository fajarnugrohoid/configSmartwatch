package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ModelSMS {
    HashMap<Integer,Date> lstAck = new HashMap<>();
    String text = "";
    Date sentTime = new Date();
    Date receivedTime = Calendar.getInstance().getTime();
    int senderId = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public  String getUID()
    {
        return  String.valueOf(senderId)+"-"+String.valueOf(sentTime.getTime());
    }

    public ModelSMS(int Sender, Date SentTime, String Message)
    {
        text = Message;
        senderId = Sender;
        sentTime = SentTime;
    }

    public  void addAck(int receiverId)
    {
        if (lstAck.containsKey(receiverId)) return;
        Date rcvdTime = Calendar.getInstance().getTime();
        lstAck.put(receiverId,rcvdTime);
    }

    public boolean isAck()
    {
        if (!lstAck.isEmpty()) return  true;
        return  false;
    }

    /*
    public AckItem[] getListAck()
    {
        List<AckItem> acks = new ArrayList<>();
        for (int receiver: lstAck.keySet()
                ) {
            AckItem item = new AckItem(receiver,lstAck.get(receiver));
            acks.add(item);
        }
        return  (AckItem[]) acks.toArray();
    } */
}
