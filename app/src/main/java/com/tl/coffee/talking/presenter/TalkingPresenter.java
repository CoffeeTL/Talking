package com.tl.coffee.talking.presenter;

import com.tl.coffee.talking.model.CmdModel;
import com.tl.coffee.talking.presenter.ibinder.ITalkingBinder;
import com.tl.coffee.talking.task.CallingThread;
import com.tl.coffee.talking.utils.GsonHolder;

/**
 * Created by coffee on 2017/12/7.
 */

public class TalkingPresenter {
    private ITalkingBinder iTalkingBinder;
    public TalkingPresenter(ITalkingBinder iTalkingBinder){
        this.iTalkingBinder = iTalkingBinder;
    }
    public void sendRequest(String serverIp,int cmd,int serial){
        CmdModel model = new CmdModel();
        model.setCmd(cmd);
        model.setSerial(serial);
        String msg = GsonHolder.get().toJson(model);
        CallingThread callingThread = new CallingThread();
        callingThread.init(serverIp,msg);
        callingThread.setCallerCallback(new CallingThread.CallerCallback() {
            @Override
            public void callback(String response) {
                iTalkingBinder.receiveData(response);
            }
        });
        callingThread.start();
    }
}
