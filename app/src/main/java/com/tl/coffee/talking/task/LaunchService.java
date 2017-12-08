package com.tl.coffee.talking.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.base.TCPServerManager;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.utils.GsonHolder;
import com.tl.coffee.talking.view.activity.TalkingActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by coffee on 2017/12/8.
 */

public class LaunchService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private boolean flag;
    @Override
    public void onCreate() {
        super.onCreate();
        flag = true;
        LaunchThread launchThread = new LaunchThread();
        launchThread.start();
    }
    private class LaunchThread extends Thread{
        private String serverIp;
        private String localIp;
        @Override
        public void run() {
            while (flag){
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
                    if(temp.length != 0){
                        deal(temp,serverIp);
                    }
                    bos.write(temp);
                    byte[] bb = bos.toByteArray();
                    if(bb.length != 0){
                        response(bb,localIp,outputStream);
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void response(byte[] bb, String localIp, OutputStream outputStream) {
        try {
            String request = new String(bb,"utf-8");
            CmdModel model = GsonHolder.get().fromJson(request,new TypeToken<CmdModel>(){}.getType());
            model.setServerIp(localIp);
            model.setSerial(1);
            String response = GsonHolder.get().toJson(model);
            byte[] resbyte = response.getBytes("utf-8");
            outputStream.write(resbyte);
            outputStream.flush();
            flag = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deal(byte[] temp, String serverIp) {
        try {
            String msg = new String(temp,"utf-8");
            CmdModel model = GsonHolder.get().fromJson(msg,new TypeToken<CmdModel>(){}.getType());
            model.setServerIp(serverIp);
            flag = false;
            if(DataConst.CMD_CALLING == model.getCmd() && 0 == model.getSerial()){
                TalkingActivity.startPage(this,model.getSerial(),model.getServerIp());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
