package com.tl.coffee.talking.utils;

/**
 * Created by coffee on 2017/12/4.
 */

public class SpeexUtils {
    private static final int DEFAULT_COMPRESSION = 5;

    static {
        try {
            System.loadLibrary("speex");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static SpeexUtils speexUtil = null;

    SpeexUtils() {
        open(DEFAULT_COMPRESSION);
    }

    public static SpeexUtils getInstance(){
        if (speexUtil == null) {
            synchronized (SpeexUtils.class) {
                if (speexUtil == null) {
                    speexUtil = new SpeexUtils();
                }
            }
        }
        return speexUtil;
    }

    public native int open(int compression);

    public native int getFrameSize();

    public native int decode(byte encoded[], short lin[], int size);

    public native int encode(short lin[], int offset, byte encoded[], int size);

    public native void close();
}
