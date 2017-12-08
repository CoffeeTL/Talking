package com.tl.coffee.talking.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tl.coffee.talking.task.LaunchService;

/**
 * Created by coffee on 2017/12/8.
 */

public class LaunchReceiver extends BroadcastReceiver {
    private static final String action = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("callingService","action = "+intent.getAction());
        if(TextUtils.equals(action,intent.getAction())){
            Intent toLaunch = new Intent(context,LaunchService.class);
            context.startService(toLaunch);
        }
    }
}
