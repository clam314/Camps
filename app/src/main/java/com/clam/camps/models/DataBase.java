package com.clam.camps.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clam314 on 2016/3/2.
 */
public class DataBase {
    public static final String DB_NAME = "DB";

    public static final int VERSION = 1;

    private  static DataBase mDataBase;
    private SQLiteDatabase db;

    private DataBase(Context context){
        DataBaseHelper dbHelper = new DataBaseHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DataBase getDataBase(Context context){
        if(mDataBase == null){
           mDataBase = new DataBase(context);
        }
        return mDataBase;
    }

    public List<String> loadData(String tableName, String colunmName){
        List<String> list = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                String set = cursor.getString(cursor.getColumnIndex(colunmName));
                list.add(set);
            }while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }

    public void saveTheLastData(String data){
        if (data == null){
            db.delete("Last",null,null);
        }
        ContentValues values = new ContentValues();
        values.put("response",data);
        db.insert("Last",null,values);
    }
}
