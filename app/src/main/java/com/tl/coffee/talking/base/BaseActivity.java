package com.tl.coffee.talking.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.task.CallingServer;
import com.tl.coffee.talking.task.TalkingServer;
import com.tl.coffee.talking.utils.GsonHolder;

/**
 * Created by coffee on 2017/12/6.
 */

public abstract class BaseActivity extends AppCompatActivity {
    //private CallingServer callingServer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        callingServer = new CallingServer();
//        callingServer.listen(true);
//        callingServer.registerReceiveListener(this);
//        callingServer.start();
    }

//    @Override
//    public void receiveMsg(String receiveFromCaller) {
//        Log.i("callService","receive from caller : "+receiveFromCaller);
//        CmdModel model = GsonHolder.get().fromJson(receiveFromCaller,new TypeToken<CmdModel>(){}.getType());
//        callBackToPageFromServer(model);
//    }

    //protected abstract void callBackToPageFromServer(CmdModel model);

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
