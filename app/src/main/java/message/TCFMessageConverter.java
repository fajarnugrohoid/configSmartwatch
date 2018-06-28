package message;

/**
 * Created by user on 19/04/2018.
 */

public class TCFMessageConverter {

    public static  byte[] ConvertMessage(TCFMessage message)
    {
        System.out.println("ConvertMessage : "+message);
        Byte[] b = null;
        /*
        switch ( message.getMessageType())
        {
            case ContollerStatus: b= ConvertControllerStatus((ControllerStatusMessage) message); break;
            case ShooterStatus:  b=ConvertShooterStatus((ShooterStatusMessage) message); break;
            case ObserverStatus: b=ConvertObserverStatus((ObserverStatusMessage) message); break;
            case WeatherStatus: b=ConvertWeather((WeatherStatusMessage) message); break;
            case TextMessage: b=ConvertText((TextMessage) message); break;
            case TacticalObject: b=ConvertObjectPosition((TacticalObjectMessage) message); break;
            case FireOrder: b=ConvertFireOrder((FireOrderMessage) message); break;
            case FireRequest: b=ConvertFireRequest((FireRequestMessage) message); break;
            case FireReply: b=ConvertFireReply((FireReplyMessage) message); break;
            case ImageData: b=ConvertImageData((ImageDataMessage) message); break;
            case CheckConnection: b=ConvertForCheckConnection((WeatherForCheckConnection)message);break;
            case CheckCCUDevice: b = ConvertForCheckCCUDevice((ForCheckCCUDevice)message);break;
            case SyncData : b = ConvertSyncFireRequest((FirerequestSyncData)message);break;
        }
        String s = bytesToHex(b); */
        String s = "xxx";
        byte[] arr = new byte[s.length()];
        char[] c = s.toCharArray();
        for (int i =0;i<c.length;i++)
        {
            arr[i] = (byte) c[i];
        }
        return arr;
    }


}
