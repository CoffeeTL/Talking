package com.tl.coffee.talking.base;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * Created by coffee on 2017/12/6.
 */

public class DataConst {
    public static final int PORT_CALLING = 58000;
    public static final int PORT_TALKING = 56001;

    public static final int TCP_SEND_MAX_SIZE = 1024;
    public static final int TCP_SEND_TIMEOUT = 6 * 1000;

    public static final int CMD_CALLING = 100;
    public static final int CMD_CALLER_CANCEL_WITHOUT_TALKING = 101;
    public static final int CMD_RECEIVER_CANCEL_WITHOUT_TALKING = 102;
    public static final int CMD_CALLER_CANCEL_WHEN_TALKING = 103;
    public static final int CMD_RECEIVER_CANCEL_WHEN_TALKING = 104;
    public static final int CMD_RECEIVER_PICKUP_AND_TALK = 105;

    public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public final static int AUDIO_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    public final static int SAMPLE_RATE_HZ = 8000;//8000, 11025, 22050, 44100, 48000
    public final static int BIT_RATE = 64000;//64000, 96000, 128000
    public final static int CHANNEL_AUDIORECOR_TYPE = AudioFormat.CHANNEL_IN_MONO;
    public final static int CHANNEL_AUDIOTRACK_TYPE = AudioFormat.CHANNEL_OUT_MONO;
    public final static int CHANNEL_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public final static int STREAM_MODE = AudioTrack.MODE_STREAM;
}
