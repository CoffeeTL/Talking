package com.tl.coffee.talking.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.base.TCPServerManager;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.utils.GsonHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by coffee on 2017/12/6.
 */

public class CallingServer extends Thread {
    private boolean flag;
    private TalkServerListener talkServerListener;
    private String serverIp;
    private String localIp;
    public void registerTalkListener(TalkServerListener talkServerListener){
        this.talkServerListener = talkServerListener;
    }
    public CallingServer(){

    }
    public void listen(boolean flag){
        this.flag = flag;
    }

    @Override
    public void run() {
        while (flag){
            receiving();
        }
    }

    private void receiving() {
        InputStream inputStream;
        OutputStream outputStream;
        ByteArrayOutputStream bos = null;
        try {
            Socket socket = TCPServerManager.getInstance().get().accept();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            serverIp = socket.getInetAddress().getHostName();
            localIp = socket.getLocalAddress().getHostName();
            bos = new ByteArrayOutputStream();
            int size = inputStream.available();
            byte[] temp = new byte[size];
            inputStream.read(temp);
            bos.write(temp);
            byte[] bb = bos.toByteArray();
            if(bb.length != 0){
                doWithRequest(bb,outputStream);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWithRequest(byte[] bb, OutputStream outputStream) {
        try {
            String request = new String(bb,"utf-8");
            Log.i("callingService","request : "+request+" serverIp : "+serverIp);
            CmdModel model = GsonHolder.get().fromJson(request,new TypeToken<CmdModel>(){}.getType());
            model.setServerIp(serverIp);
            String toclient = GsonHolder.get().toJson(model);
            talkServerListener.receiveTalkMsg(toclient);

            model.setServerIp(localIp);
            model.setSerial(1);
            String response = GsonHolder.get().toJson(model);
            byte[] resbyte = response.getBytes("utf-8");
            outputStream.write(resbyte);
            outputStream.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public interface TalkServerListener{
        void receiveTalkMsg(String receiveFromCaller);
    }
}
