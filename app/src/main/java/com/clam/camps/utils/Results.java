package com.clam.camps.utils;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.List;

/**
 * Created by clam314 on 2016/3/2.
 */
public class Results {
    public HashSet<String> category;
    public boolean error;
    public ResultList results;


    public static class ResultList{
        public List<Result> Android;
        public List<Result> iOS;
        @SerializedName("福利")
        public List<Result>  been;
        @SerializedName("前端")
        public List<Result>  front_end;
        public List<Result>  App;
    }

}