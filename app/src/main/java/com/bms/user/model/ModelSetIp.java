package com.bms.user.model;

public class ModelSetIp {
    private String SSIDName;
    private String SSIDPassword;
    private String myIp;
    private String myGateway;

    public String getSSIDName() {
        return SSIDName;
    }

    public void setSSIDName(String SSIDName) {
        this.SSIDName = SSIDName;
    }

    public String getSSIDPassword() {
        return SSIDPassword;
    }

    public void setSSIDPassword(String SSIDPassword) {
        this.SSIDPassword = SSIDPassword;
    }

    public String getMyIp() {
        return myIp;
    }

    public void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    public String getMyGateway() {
        return myGateway;
    }

    public void setMyGateway(String myGateway) {
        this.myGateway = myGateway;
    }
}
