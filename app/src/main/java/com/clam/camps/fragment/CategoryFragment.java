package com.clam.camps.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam.camps.R;
import com.clam.camps.adapter.AndroidRecycleViewAdapter;
import com.clam.camps.models.Constants;
import com.clam.camps.models.DataBase;
import com.clam.camps.utils.Category;
import com.clam.camps.utils.OkHttpUtil;
import com.clam.camps.utils.Result;
import com.clam.camps.utils.Util;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by clam314 on 2016/3/3.
 */
public class CategoryFragment extends Fragment {
    private Gson gson;
    private RecyclerView recyclerView;
    private AndroidRecycleViewAdapter adapter;
    private View view;
    private DataBase dataBase;
    private List<Result> results;
    private String query_category;


    public CategoryFragment(){

    }

    @SuppressLint("ValidFragment")
    public CategoryFragment(String category){
        query_category = Constants.REQUEST_CATEGORY+category+"/10/";
    }

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
        view = inflater.inflate(R.layout.fragment_android,container,false);
        results = null;

        recyclerView = (RecyclerView)view.findViewById(R.id.recyleview_android);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AndroidRecycleViewAdapter(getActivity(),results);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStop() {
        Log.d("Fragment", "androidFragment onstop");
        super.onStop();
        recyclerView.removeAllViews();
    }

    private Request queryAndroidData(int page){
        return new Request.Builder().url(query_category+page).build();
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
                   // String decode = Util.decodeUnicode(response.body().string());
                    String newdecode = response.body().string();
                    Category results = gson.fromJson(newdecode, Category.class);
                    if (results.results.size()!=0) {
                        adapter.setList(results.results);
                        if (getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }

            }

        };
    }

    public void refresh(){
        try {
            OkHttpUtil.execute(queryAndroidData(1), getCallback());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
