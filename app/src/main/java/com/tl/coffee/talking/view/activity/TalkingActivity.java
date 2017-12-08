package com.tl.coffee.talking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.R;
import com.tl.coffee.talking.base.BaseActivity;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.presenter.CallingPresenter;
import com.tl.coffee.talking.presenter.TalkingPresenter;
import com.tl.coffee.talking.presenter.ibinder.ITalkingBinder;
import com.tl.coffee.talking.task.CallingServer;
import com.tl.coffee.talking.task.TalkingClient;
import com.tl.coffee.talking.task.TalkingServer;
import com.tl.coffee.talking.utils.GsonHolder;
import com.tl.coffee.talking.view.ui.AnswerToggleBtn;

import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by coffee on 2017/12/7.
 */

public class TalkingActivity extends BaseActivity implements ITalkingBinder,CallingServer.TalkServerListener{
    private ImageView bg_iv;
    private TextView tv_name;
    private TextView tv_state;
    private TextView tv_hungUp;
    private AnswerToggleBtn toggleBtn;
    private int serial;
    private String serverIp;
    private TalkingPresenter talkingPresenter;
    private DisplayHandler handler = new DisplayHandler(this);
    private TalkingServer talkingServer;
    private TalkingClient talkingClient;
    private CallingServer callingServer;

    public static void  startPage(Context context,int serial,String serverIp){
        Intent totalk = new Intent(context,TalkingActivity.class);
        totalk.putExtra("call_serial",serial);
        totalk.putExtra("call_serverIp",serverIp);
        totalk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(totalk);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taiking);
        bindView();
        initView();
    }

    private void initView() {
        callingServer = new CallingServer();
        callingServer.listen(true);
        callingServer.registerTalkListener(this);
        callingServer.start();
        talkingPresenter = new TalkingPresenter(this);
        serial = getIntent().getIntExtra("call_serial",-1);
        serverIp = getIntent().getStringExtra("call_serverIp");
        Glide.with(this).load(R.mipmap.calling_bg).crossFade(500).bitmapTransform(new BlurTransformation(this,23,4)).into(bg_iv);
        tv_name.setText(serverIp);
        if(1 == serial){
            tv_state.setText("Calling...");
            hideToggleBtn();
        }else{
            tv_state.setText("You got a call...");
            hideHungBtn();
            toggleBtn.setOnToggleListener(new AnswerToggleBtn.ToggleListener() {
                @Override
                public void onPickup() {
                    talkingPresenter.sendRequest(serverIp, DataConst.CMD_RECEIVER_PICKUP_AND_TALK,0);
                    isTalking = true;
                    hideToggleBtn();
                }
                @Override
                public void onReject() {
                    talkingPresenter.sendRequest(serverIp, DataConst.CMD_RECEIVER_CANCEL_WITHOUT_TALKING,0);
                }
            });
        }
        talkingServer = new TalkingServer();
        talkingServer.init();
        talkingServer.start();
    }
    private void hideHungBtn() {
        if(toggleBtn.getVisibility() == View.GONE) toggleBtn.setVisibility(View.VISIBLE);
        if(tv_hungUp.getVisibility() == View.VISIBLE) tv_hungUp.setVisibility(View.GONE);
    }
    private boolean isTalking;
    private void hideToggleBtn() {
        if(tv_hungUp.getVisibility() == View.GONE) tv_hungUp.setVisibility(View.VISIBLE);
        if(toggleBtn.getVisibility() == View.VISIBLE) toggleBtn.setVisibility(View.GONE);
        tv_hungUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(1 == serial){
                    if(!isTalking){
                        talkingPresenter.sendRequest(serverIp, DataConst.CMD_CALLER_CANCEL_WITHOUT_TALKING,0);
                    }else{
                        talkingPresenter.sendRequest(serverIp, DataConst.CMD_CALLER_CANCEL_WHEN_TALKING,0);
                    }
                }else if(0 == serial){
                    talkingPresenter.sendRequest(serverIp, DataConst.CMD_RECEIVER_CANCEL_WHEN_TALKING,0);
                }
            }
        });
    }

    private void bindView() {
        bg_iv = findViewById(R.id.activity_talking_bgiv);
        tv_name = findViewById(R.id.activity_talking_name);
        tv_state = findViewById(R.id.activity_talking_state);
        tv_hungUp = findViewById(R.id.activity_talking_hangup_btn);
        toggleBtn = findViewById(R.id.activity_talking_togglebtn);
    }

    @Override
    public void receiveData(String msg) {
        Log.i("callingService",msg);
        CmdModel model = GsonHolder.get().fromJson(msg,new TypeToken<CmdModel>(){}.getType());
        Message message = new Message();
        message.obj = model;
        handler.sendMessage(message);
    }
    private Timer timer;
    private void finishPage() {
        if(timer == null) timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                endCall();
                finish();
            }
        },1500);
    }

    private void endCall() {
        if(talkingClient != null) talkingClient.free();
        talkingClient = null;
        if(talkingServer != null) talkingServer.free();
        talkingServer = null;
    }

    @Override
    public void receiveTalkMsg(String receiveFromCaller) {
        CmdModel model = GsonHolder.get().fromJson(receiveFromCaller,new TypeToken<CmdModel>(){}.getType());
        Log.i("callingService","model : "+model.getCmd()+" ");
        Message msg = new Message();
        msg.obj = model;
        handler.sendMessage(msg);
    }

    private static class DisplayHandler extends Handler{
        private TalkingActivity talkingActivity;
        public DisplayHandler(TalkingActivity talkingActivity){
            this.talkingActivity = talkingActivity;
        }
        @Override
        public void handleMessage(Message msg) {
            talkingActivity.dealWithMsg((CmdModel) msg.obj);
        }
    }
    private void dealWithMsg(CmdModel model){
        switch(model.getCmd()){
            case DataConst.CMD_CALLER_CANCEL_WITHOUT_TALKING:
                if(1 == model.getSerial()) tv_state.setText("Hang up");
                else if(0 == model.getSerial()) tv_state.setText("the caller has canceled the call");
                finishPage();
                break;
            case DataConst.CMD_RECEIVER_CANCEL_WITHOUT_TALKING:
                if(1 == model.getSerial()) tv_state.setText("You have hung up the call");
                else if(0 == model.getSerial())tv_state.setText("Your call has been hung up");
                finishPage();
                break;
            case DataConst.CMD_CALLER_CANCEL_WHEN_TALKING:
                if(1 == model.getSerial()) tv_state.setText("Hang up");
                else if(0 == model.getSerial()) tv_state.setText("the caller has canceled the call");
                finishPage();
                break;
            case DataConst.CMD_RECEIVER_CANCEL_WHEN_TALKING:
                if(1 == model.getSerial()) tv_state.setText("You have hung up the call");
                else if(0 == model.getSerial())tv_state.setText("Your call has been hung up");
                finishPage();
                break;
            case DataConst.CMD_RECEIVER_PICKUP_AND_TALK:
                tv_state.setText("Talking...");
                talkingClient = new TalkingClient();
                talkingClient.init(model.getServerIp());
                talkingClient.start();
                break;

        }

    }
}
