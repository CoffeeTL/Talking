package com.tl.coffee.talking.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tl.coffee.talking.R;
import com.tl.coffee.talking.base.BaseActivity;
import com.tl.coffee.talking.base.DataConst;
import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.presenter.CallingPresenter;
import com.tl.coffee.talking.presenter.ibinder.ICallingBinder;
import com.tl.coffee.talking.utils.GsonHolder;
import com.tl.coffee.talking.view.adapter.IpAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by coffee on 2017/12/6.
 */

public class CallingActivity extends BaseActivity implements ICallingBinder{
    @BindView(R.id.activity_calling_recycle) RecyclerView recyclerView;
    private CallingPresenter callingPresenter;
    private List<String> dataList;
    private IpAdapter ipAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        ButterKnife.bind(this);
        bindView();
        loadData();
        callingPresenter = new CallingPresenter(this);
    }

    private void loadData() {
        dataList = new ArrayList<>();
        dataList.add("192.168.136.2");
        ipAdapter = new IpAdapter(this,dataList);
        recyclerView.setAdapter(ipAdapter);
        ipAdapter.notifyDataSetChanged();

    }

    private void bindView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void receiveData(String data) {
        Log.i("callingService","receiveData : "+data);
        CmdModel model = GsonHolder.get().fromJson(data,new TypeToken<CmdModel>(){}.getType());
        if(DataConst.CMD_CALLING == model.getCmd() && 1 == model.getSerial()){
            TalkingActivity.startPage(this,model.getSerial(),model.getServerIp());
        }
    }

    public void clickIten(String ip) {
        callingPresenter.sendRequest(ip,DataConst.CMD_CALLING,0);
    }

}
