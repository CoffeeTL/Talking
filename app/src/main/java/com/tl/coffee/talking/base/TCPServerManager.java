package com.tl.coffee.talking.base;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by coffee on 2017/12/6.
 */

public class TCPServerManager {
    private static TCPServerManager tcpServerManager = null;
    private static ServerSocket serverSocket = null;
    public static TCPServerManager getInstance(){
        if(null == tcpServerManager){
            tcpServerManager = new TCPServerManager();
        }
        return tcpServerManager;
    }
    public ServerSocket get(){
        if(null == serverSocket){
            try {
                serverSocket = new ServerSocket(DataConst.PORT_CALLING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverSocket;
    }
}
