package com.tl.coffee.talking.base;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by coffee on 2017/12/6.
 */

public class UDPServerManager  {
    private static UDPServerManager udpServerManager = null;
    private static DatagramSocket datagramSocket;
    public static UDPServerManager getInstance(){
        if(null == udpServerManager){
            udpServerManager = new UDPServerManager();
        }
        return udpServerManager;
    }
    public DatagramSocket socket(){
        if(datagramSocket == null){
            try {
                datagramSocket = new DatagramSocket(DataConst.PORT_TALKING);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return datagramSocket;
    }
}
