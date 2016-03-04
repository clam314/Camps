package com.clam.camps.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clam.camps.R;
import com.clam.camps.adapter.AndroidRecycleViewAdapter;
import com.clam.camps.adapter.BeenRecyclerViewAdapter;
import com.clam.camps.models.Constants;
import com.clam.camps.models.DataBase;
import com.clam.camps.utils.Actegory;
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
 * Created by clam314 on 2016/3/4.
 */
public class BeenFragment extends Fragment {
    private Gson gson;
    private RecyclerView recyclerView;
    private BeenRecyclerViewAdapter adapter;
    private View view;
    private DataBase dataBase;
    private List<Result> results;
    private String query_category;
    private String query_category_ramdon;


    public BeenFragment(){

    }

    @SuppressLint("ValidFragment")
    public BeenFragment(String category){
        query_category = Constants.REQUEST_CATEGORY+category+"/10/";
        query_category_ramdon = Constants.REQUEST_CATEGORY_RANDOM+category+"/";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataBase = DataBase.getDataBase(getContext());
        gson = new Gson();
        try {
            OkHttpUtil.execute(queryAndroidData(1), getCallback());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_android,container,false);
        results = null;
        // List<String> hasData = dataBase.loadData("Last","response");
        //   if(hasData.size() != 0){
        //     results = gson.fromJson(hasData.get(0),Results.class);
        // }
        recyclerView = (RecyclerView)view.findViewById(R.id.recyleview_android);
       // recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        adapter = new BeenRecyclerViewAdapter(getActivity(),results,this);
        recyclerView.setAdapter(adapter);
        return view;
    }


    public View getMView(){
        return view;
    }
    private Request queryAndroidData(int page){
        return new Request.Builder().url(query_category+page).build();
    }

    private Request querRandomData(int num){
        return new Request.Builder().url(query_category_ramdon+num).build();
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
                    String decode = Util.decodeUnicode(response.body().string());
                    // Log.d("result","decode "+decode);
                    Actegory results = gson.fromJson(decode, Actegory.class);
                    if (results.results.size()!=0) {
                        adapter.setList(results.results);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

            }

        };
    }}
