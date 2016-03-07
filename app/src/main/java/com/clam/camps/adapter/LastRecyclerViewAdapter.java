package com.clam.camps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clam.camps.R;
import com.clam.camps.utils.Result;
import com.clam.camps.utils.Results;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * Created by clam314 on 2016/3/2.
 */
public class LastRecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity activity;
    private Results resultslist;
    private LayoutInflater inflater;
    public LastRecyclerViewAdapter(Activity activity, Results results){
        resultslist = results;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void setResults(Results results){
        resultslist = results;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(resultslist!=null&&resultslist.category.contains("Android")){
            size++;
        }
        if(resultslist!=null&&resultslist.category.contains("iOS")){
            size++;
        }
        if(resultslist!=null&&resultslist.category.contains("App")){
            size++;
        }
        if(resultslist!=null&&resultslist.category.contains("福利")){
            size++;
        }
        if (resultslist!=null&&resultslist.category.contains("前端")){
            size++;
        }
        return size;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0){
            return new CardImageHolder(inflater.inflate(R.layout.card_last_image,parent,false));
        }else {
            return new CardHolder(inflater.inflate(R.layout.card_last,parent,false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                if(resultslist.results.been != null){
                    return 0;
                }
            case 1:
                if(resultslist.results.Android != null){
                    return 1;
                }
            case 2:
                if (resultslist.results.iOS != null){
                    return 2;
                }
            case 3:
                if (resultslist.results.front_end!=null){
                    return 3;
                }
            default:
                if (resultslist.results.App!=null){
                    return 4;
                }

        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int mViewType = getItemViewType(position);

        switch (mViewType){
            case 0:
                CardImageHolder holder0 = (CardImageHolder)holder;
                holder0.draweeView.setImageURI(Uri.parse(resultslist.results.been.get(0).getUrl()));
                holder0.textView_title.setText(resultslist.results.been.get(0).getType());
                holder0.textView_date.setText(resultslist.results.been.get(0).getPublishedAt().substring(0,10));
                break;
            case 1:
                CardHolder holder1 = (CardHolder)holder;
                holder1.imageView_title.setImageResource(R.color.green);
                setHolder(holder1, resultslist.results.Android);
                break;
            case 2:
                CardHolder holder2 = (CardHolder)holder;
                holder2.imageView_title.setImageResource(R.color.red);
                setHolder(holder2,resultslist.results.iOS);
                break;
            case 3:
                CardHolder holder3 = (CardHolder)holder;
                holder3.imageView_title.setImageResource(R.color.coffee);
                setHolder(holder3,resultslist.results.front_end);
                break;
            case 4:
                CardHolder holder4 = (CardHolder)holder;
                holder4.imageView_title.setImageResource(R.color.yellow);
                setHolder(holder4, resultslist.results.App);
                break;
        }
    }


    private static class CardImageHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public TextView textView_title;
        public TextView textView_date;
        public SimpleDraweeView draweeView;
        public CardImageHolder(View itemView){
            super(itemView);
            textView_title = (TextView)itemView.findViewById(R.id.tv_title);
            textView_date = (TextView)itemView.findViewById(R.id.tv_date);
            draweeView = (SimpleDraweeView)itemView.findViewById(R.id.draweeview_last);
        }
    }

    private static class CardHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public TextView textView_title;
        public TextView textView_date;
        public ImageView imageView_title;
        public CardHolder(View itemView){
            super(itemView);
            imageView_title = (ImageView)itemView.findViewById(R.id.iv_card_title);
            textView_title = (TextView)itemView.findViewById(R.id.tv_title);
            textView_date = (TextView)itemView.findViewById(R.id.tv_date);
            layout = (LinearLayout)itemView.findViewById(R.id.ly_last);
        }
    }

    private void setHolder(CardHolder holder,List<Result> list){
        holder.textView_title.setText(list.get(0).getType());
        holder.textView_date.setText(list.get(0).getPublishedAt().substring(0, 10));
        setItem(holder.layout, list);
    }
    private void setItem(LinearLayout layout, final List<Result> list){
        TextView textView_item = null;
        LinearLayout.LayoutParams params = null;
        layout.removeAllViewsInLayout();
        for (int i = 0; i<list.size();i++){
            final int item = i;
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            params.bottomMargin = 10;
            textView_item = new TextView(activity);
            textView_item.setText(list.get(i).getDesc()+"("+list.get(i).getWho()+")");
            textView_item.setTextColor(Color.BLACK);
            textView_item.setTextSize(16);
            textView_item.setLayoutParams(params);
            textView_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(list.get(item).getUrl()));
                    activity.startActivity(intent);
                }
            });
            layout.addView(textView_item);
        }

    }
}
