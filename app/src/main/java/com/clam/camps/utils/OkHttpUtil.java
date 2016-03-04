package com.clam.camps.utils;



import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by clam314 on 2016/3/1.
 */
public class OkHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static{
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30,TimeUnit.SECONDS);
    }

    /**同步请求*/
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }

    /**异步请求*/
    public  static void execute(Request request,Callback callback) throws IOException{
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
