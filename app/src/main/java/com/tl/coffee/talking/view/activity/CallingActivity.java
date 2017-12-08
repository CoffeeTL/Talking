package com.tl.coffee.talking.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.R;
import com.tl.coffee.talking.base.BaseActivity;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.presenter.CallingPresenter;
import com.tl.coffee.talking.presenter.ibinder.ICallingBinder;
import com.tl.coffee.talking.task.CallingServer;
import com.tl.coffee.talking.task.CallingThread;
import com.tl.coffee.talking.task.TalkingClient;
import com.tl.coffee.talking.task.TalkingServer;
import com.tl.coffee.talking.utils.GsonHolder;

/**
 * Created by coffee on 2017/12/6.
 */

public class CallingActivity extends BaseActivity implements View.OnClickListener,ICallingBinder{
    private Button btn_btn;
    private String serverIp = "192.168.136.3";//the ip you want to communicate
    private CallingPresenter callingPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        bindView();
        callingPresenter = new CallingPresenter(this);
    }
    private void bindView() {
        btn_btn = findViewById(R.id.activity_calling_btn_call);
        btn_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.activity_calling_btn_call){
            callingPresenter.sendRequest(serverIp,DataConst.CMD_CALLING,0);
        }
    }

    @Override
    public void receiveData(String data) {
        Log.i("callingService","receiveData : "+data);
        CmdModel model = GsonHolder.get().fromJson(data,new TypeToken<CmdModel>(){}.getType());
        if(DataConst.CMD_CALLING == model.getCmd() && 1 == model.getSerial()){
            TalkingActivity.startPage(this,model.getSerial(),model.getServerIp());
        }
    }
}
