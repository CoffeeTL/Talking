package com.tl.coffee.talking.model;

/**
 * Created by coffee on 2017/12/6.
 */

public class CmdModel {
    private int cmd;
    private int serial;
    private String serverIp;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
