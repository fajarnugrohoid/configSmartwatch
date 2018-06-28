package com.bms.user.bmssmartwatch;

/**
 * Created by user on 20/04/2018.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SBCAdapter {

    static InetAddress ia;

    public static boolean checkSbcAddress(String ip_add) {
        try {
            ia = InetAddress.getByName(ip_add);
            if(ia.isReachable(5000)) {
                return true;
            } else {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

}
