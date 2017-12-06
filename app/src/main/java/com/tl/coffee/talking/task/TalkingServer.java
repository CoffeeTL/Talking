package com.tl.coffee.talking.task;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.base.UDPServerManager;
import com.tl.coffee.talking.utils.SpeexUtils;

import java.io.IOException;
import java.net.DatagramPacket;

import static android.content.ContentValues.TAG;

/**
 * Created by coffee on 2017/12/6.
 */

public class TalkingServer extends Thread {
    private AudioTrack audioTrack;
    private int audio_buffer_size;
    public void init(){
        audio_buffer_size = AudioTrack.getMinBufferSize(DataConst.SAMPLE_RATE_HZ, DataConst.CHANNEL_AUDIOTRACK_TYPE , AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(DataConst.AUDIO_STREAM_TYPE,
                DataConst.SAMPLE_RATE_HZ,DataConst.CHANNEL_AUDIOTRACK_TYPE,DataConst.CHANNEL_FORMAT,audio_buffer_size,DataConst.STREAM_MODE);
        audioTrack.setStereoVolume(1.0f, 1.0f);
        audioTrack.play();
    }
    public void free() {
        SpeexUtils.getInstance().close();
        try {
            Thread.sleep(1000) ;
        } catch(Exception e) {
            Log.d("sleep exceptions...\n","") ;
        }
    }

    @Override
    public void run() {
        while (true){
            receive();
        }
    }

    private void receive() {
        byte[] buf = new byte[1024];
        short[] rcvProcessedData = new short[160];
        byte[]  rawData= new byte[256];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            UDPServerManager.getInstance().socket().receive(packet);
            System.arraycopy(packet.getData(), 0, rawData, 0, packet.getLength());
            int res;
            synchronized (audioTrack){
                res = SpeexUtils.getInstance().decode(rawData,rcvProcessedData,160);
            }
            if(res > 0){
                Log.i(TAG,"decoded data = "+res);
                for (int i = 0; i < rcvProcessedData.length; i++) {
                    rcvProcessedData[i] = (short) (rcvProcessedData[i]*2);
                }
                audioTrack.write(rcvProcessedData,0,res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
