package com.clam.camps.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clam.camps.R;
import com.clam.camps.adapter.LastRecyclerViewAdapter;
import com.clam.camps.models.Constants;
import com.clam.camps.models.DataBase;
import com.clam.camps.utils.OkHttpUtil;
import com.clam.camps.utils.Result;
import com.clam.camps.utils.Results;
import com.clam.camps.utils.Util;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by clam314 on 2016/3/1.
 */
public class LastFragment extends Fragment  {

    private Gson gson;
    private RecyclerView recyclerView;
    private LastRecyclerViewAdapter adapter;
    private  View view;
    private DataBase dataBase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataBase = DataBase.getDataBase(getContext());
        gson = new Gson();
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_last,container,false);
        Results results = null;
        List<String> hasData = dataBase.loadData("Last","response");
        if(hasData.size() != 0){
            results = gson.fromJson(hasData.get(0),Results.class);
        }
        recyclerView = (RecyclerView)view.findViewById(R.id.recyleview_last);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LastRecyclerViewAdapter(getActivity(),results);
        recyclerView.setAdapter(adapter);
        return view;
    }



    private Request queryTodayData(){
        return new Request.Builder().url(Constants.REQUEST_TO_DAY_DATA+getTodayTime()).build();
    }

    private String getTodayTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    private Callback getCallback(){
      return   new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (!isAdded()) return ;
                if (response.isSuccessful()) {
                  //  String newdecode = new String(response.body().bytes(),"UTF-8");
                   // String decode = Util.decodeUnicode(response.body().string());
                    String newdecode = response.body().string();
                    Results results = gson.fromJson(newdecode, Results.class);
                    if (results.category.size() != 0) {
                        dataBase.saveTheLastData(newdecode);
                        adapter.setResults(results);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "今日干货没到位", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }

        };
    }

    public void refresh(){
        try {
            OkHttpUtil.execute(queryTodayData(),getCallback());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
