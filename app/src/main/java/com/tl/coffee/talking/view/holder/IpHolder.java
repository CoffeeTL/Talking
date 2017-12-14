package com.tl.coffee.talking.view.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tl.coffee.talking.R;
import com.tl.coffee.talking.view.activity.CallingActivity;

import butterknife.BindView;

/**
 * Created by coffee on 2017/12/14.
 */

public class IpHolder extends RecyclerView.ViewHolder {
    private Context context;
    @BindView(R.id.item_calling_tv)TextView tv_ip;
    public IpHolder(Context contex, View itemView) {
        super(itemView);
        this.context = contex;
    }
    public void setData(String ip){
        tv_ip.setText(ip);
        tv_ip.setOnClickListener(v->{
            if(context instanceof CallingActivity){
                CallingActivity ca = (CallingActivity) context;
                ca.clickIten(ip);
            }
        });
    }
}
