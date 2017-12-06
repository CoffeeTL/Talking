package com.tl.coffee.talking.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tl.coffee.talking.R;
import com.tl.coffee.talking.base.BaseActivity;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.task.CallingServer;
import com.tl.coffee.talking.task.CallingThread;
import com.tl.coffee.talking.task.TalkingClient;
import com.tl.coffee.talking.task.TalkingServer;
import com.tl.coffee.talking.utils.GsonHolder;

/**
 * Created by coffee on 2017/12/6.
 */

public class CallingActivity extends BaseActivity implements View.OnClickListener{
    private EditText editText;
    private Button btn_btn;
    private CallHandler handler = new CallHandler(this);
    private String localIp = "192.168.136.2";
    private String serverIp = "192.168.136.3";
    private TalkingClient talkingClient;
    private TalkingServer talkingServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        bindView();
        CallingServer callingServer = new CallingServer(handler);
        callingServer.listen(true);
        callingServer.start();
        talkingServer = new TalkingServer();
        talkingServer.init();
        talkingServer.start();
    }

    private void bindView() {
        editText = findViewById(R.id.activity_calling_edit);
        btn_btn = findViewById(R.id.activity_calling_btn_call);
        btn_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_calling_btn_call){
            CmdModel model = new CmdModel();
            model.setCmd(DataConst.CMD_CALLING);
            model.setSerial(0);
            final String msg = GsonHolder.get().toJson(model);
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    CallingThread callingThread = new CallingThread(handler);
                    callingThread.init(serverIp,msg);
                    callingThread.start();
                }
            });
            task.start();

        }
    }
    private static class CallHandler extends Handler{
        private CallingActivity callingActivity;
        public CallHandler(CallingActivity callingActivity){
            this.callingActivity = callingActivity;
        }
        @Override
        public void handleMessage(Message msg) {
            callingActivity.handleMsg(msg);
        }
    }
    public void handleMsg(Message msg){
        String response = (String) msg.obj;
        editText.setText(response);
        talkingClient = new TalkingClient();
        talkingClient.init(serverIp);
        talkingClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(talkingClient != null){
            talkingClient.free();
        }
        if(talkingServer != null){
            talkingServer.free();
        }
        talkingClient = null;
        talkingServer = null;
    }
}
