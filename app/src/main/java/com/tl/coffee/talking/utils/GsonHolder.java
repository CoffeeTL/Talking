package com.tl.coffee.talking.utils;

import com.google.gson.Gson;

/**
 * Created by coffee on 2017/12/6.
 */

public class GsonHolder {
    private static Gson gson = null;
    public static Gson get(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }
}
