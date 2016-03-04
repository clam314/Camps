package com.clam.camps.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by clam314 on 2016/3/2.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    //创建每日列表的table
    public static final String CREATE_TODAY_LIST = "create table Last ("
            + "key_id integer primary key autoincrement, "
            + "response text"
            + ")";



    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODAY_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
