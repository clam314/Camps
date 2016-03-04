package com.clam.camps.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clam.camps.R;
import com.clam.camps.utils.Result;

import java.util.List;

/**
 * Created by clam314 on 2016/3/3.
 */
public class AndroidRecycleViewAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private List<Result> list;
    private LayoutInflater inflater;

    public AndroidRecycleViewAdapter(Activity activity, List<Result> list){
        this.activity  = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
    }

    public void setList(List<Result> list){
        this.list = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("adapter","onBindViewHolder");
        ItemHolder itemHolder = (ItemHolder)holder;
        Log.d("list", " onbind  "+list.get(position).getDesc());
        itemHolder.textView_desc.setText(list.get(position).getDesc());
        itemHolder.textView_time.setText(list.get(position).getPublishedAt().substring(5, 10));
        itemHolder.textView_who.setText(list.get(position).getWho());
    }

    @Override
    public int getItemCount() {
        Log.d("adapter","getItemCount");

        return list!=null?list.size():0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.card_android,parent,false));
    }

    private class ItemHolder extends RecyclerView.ViewHolder{
        private TextView textView_who;
        private TextView textView_time;
        private TextView textView_desc;
        private ImageView imageView_star;

        public ItemHolder(View itemView){
            super(itemView);
            textView_who = (TextView)itemView.findViewById(R.id.tv_who);
            textView_time = (TextView)itemView.findViewById(R.id.tv_time);
            textView_desc = (TextView)itemView.findViewById(R.id.tv_desc);
        }
    }
}
