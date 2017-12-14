package com.tl.coffee.talking.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tl.coffee.talking.R;
import com.tl.coffee.talking.view.holder.IpHolder;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by coffee on 2017/12/14.
 */

public class IpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> dataList;
    private LayoutInflater inflater;
    private static int TYPE_IP = 1;
    public IpAdapter(Context context, List<String> dataList){
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_IP == viewType){
            View ipview = inflater.inflate(R.layout.item_calling,null);
            IpHolder holder = new IpHolder(context,ipview);
            ButterKnife.bind(holder,ipview);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof IpHolder){
            ((IpHolder) holder).setData(dataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_IP;
    }
}
