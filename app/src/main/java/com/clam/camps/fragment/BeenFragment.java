package com.clam.camps.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clam.camps.R;
import com.clam.camps.adapter.BeenRecyclerViewAdapter;
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
    private String query_category_random;
    private boolean random;
    private GridLayoutManager gridLayoutManager;
    private int page = 1;

    public BeenFragment(){

    }

    @SuppressLint("ValidFragment")
    public BeenFragment(String category,boolean random){
        query_category = Constants.REQUEST_CATEGORY+category+"/10/";
        query_category_random = Constants.REQUEST_CATEGORY_RANDOM+category+"/";
        this.random = random;
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
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        adapter = new BeenRecyclerViewAdapter(getActivity(),results,this);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
       if (!random){
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener());
       }
        return view;
    }




    private Request queryAndroidData(int page){
        return new Request.Builder().url(query_category+page).build();
    }

    private Request queryRandomData(int num){
        return new Request.Builder().url(query_category_random +num).build();
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
                    String newdecode = response.body().string();
                   // String decode = Util.decodeUnicode(response.body().string());
                    // Log.d("result","decode "+decode);
                    Category results = gson.fromJson(newdecode, Category.class);
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page = 1;
    }

    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener{
        private boolean loading = false;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            final int totalItemCount = gridLayoutManager.getItemCount();
            if(!loading && lastVisibleItem*2 >= totalItemCount-1){
                Log.d("scroll","scroll "+totalItemCount+" "+lastVisibleItem);
                loading = true;
                try {
                    Request request = queryAndroidData(++page);
                    OkHttpUtil.execute(request, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {

                            loading = false;
                            page--;
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (!isAdded()) return ;
                            if (response.isSuccessful()) {
                                loading = false;
                                String decode = Util.decodeUnicode(response.body().string());
                                Category results = gson.fromJson(decode, Category.class);
                                if (results.results.size()!=0) {
                                    adapter.addList(results.results);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyItemInserted(totalItemCount);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if (totalItemCount>20 && totalItemCount - 1 <= lastVisibleItem){
                Toast.makeText(getActivity(),"没有更多了~",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refresh(){
        try {
            if(random){
                OkHttpUtil.execute(queryRandomData(20), getCallback());
            }else {
                page = 1;
                OkHttpUtil.execute(queryAndroidData(1), getCallback());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
