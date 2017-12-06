package com.tl.coffee.talking.task;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.util.Log;

import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.base.UDPServerManager;
import com.tl.coffee.talking.utils.SpeexUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by coffee on 2017/12/6.
 */

public class TalkingClient extends Thread {
    protected boolean m_keep_running;
    private static final String TAG = "talkingClient";
    private String serverip;
    private AudioRecord recorder;
    private int bufferSize;
    public void init(String serverip) {
        this.serverip = serverip;
        Log.i(TAG,"serverIp = "+serverip);
        m_keep_running = true ;
        bufferSize = AudioRecord.getMinBufferSize(DataConst.SAMPLE_RATE_HZ, DataConst.CHANNEL_AUDIORECOR_TYPE, DataConst.CHANNEL_FORMAT);
        Log.i(TAG,"bufferSize = "+bufferSize);
        recorder = new AudioRecord(DataConst.AUDIO_SOURCE,DataConst.SAMPLE_RATE_HZ, DataConst.CHANNEL_AUDIORECOR_TYPE,DataConst.CHANNEL_FORMAT, bufferSize );
        recorder.startRecording();
    }
    public void free() {
        m_keep_running = false ;
        SpeexUtils.getInstance().close();
        try {
            Thread.sleep(1000) ;
        } catch(Exception e) {
            Log.d("sleep exceptions...\n","") ;
        }
    }

    @Override
    public void run() {
        while (m_keep_running){
            short[] bufferRead = new short[160];
            byte[] processedData = new byte[1024];
            short[] rawdata = new short[1024];
            int bufferReadResult = recorder.read(bufferRead, 0,160);
            Log.i(TAG,"len = "+bufferReadResult);
            if(bufferReadResult > 0){
                synchronized (recorder) {
                    System.arraycopy(bufferRead, 0,rawdata , 0, bufferReadResult);
                    int count = SpeexUtils.getInstance().encode(rawdata,0,processedData,bufferReadResult);
                    if(count > 0){
                        DatagramPacket packet = null;
                        try {
                            packet = new DatagramPacket(processedData,count, InetAddress.getByName(serverip), DataConst.PORT_TALKING);
                            UDPServerManager.getInstance().socket().send(packet);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }
    }
}
