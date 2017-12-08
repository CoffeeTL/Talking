package com.tl.coffee.talking.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.utils.GsonHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by coffee on 2017/12/6.
 */

public class CallingThread extends Thread {
    private String serverIp;
    private String data;
    private CallerCallback listener;
    public void setCallerCallback(CallerCallback listener){
        this.listener = listener;
    }
    public CallingThread(){
    }
    public void init(String serverIp,String data){
        this.serverIp = serverIp;
        this.data = data;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        try {
            Socket socket = new Socket(serverIp, DataConst.PORT_CALLING);
            outputStream = socket.getOutputStream();
            Log.i("callingService","data:"+data+" time : "+System.currentTimeMillis());
            byte[] bb = data.getBytes("utf-8");
            outputStream.write(bb);
            outputStream.flush();
            ReceiverThread receiverThread = new ReceiverThread(socket);
            receiverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class ReceiverThread extends Thread{
        Socket socket = null;
        public ReceiverThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            while (true){
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    int ava = inputStream.available();
                    byte[] bb = new  byte[ava];
                    inputStream.read(bb);
                    if(bb.length > 0){
                        handleMsg(bb);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleMsg(byte[] bb) {
        try {
            String msg = new String(bb,"utf-8");
            Log.i("callingService","cmd : "+msg);
            listener.callback(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public interface CallerCallback{
        void callback(String response);
    }
}
