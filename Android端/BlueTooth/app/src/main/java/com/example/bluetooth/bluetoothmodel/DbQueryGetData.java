package com.example.bluetooth.bluetoothmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.bluetooth.DatabaseShowActivity;
import com.example.bluetooth.db.DateBaseHelper;


/**
 * @author chenweibin
 * @date 2020-1-5 19:50:51
 */
public class DbQueryGetData {
    private SQLiteDatabase database;
    private Activity activity;

    public DbQueryGetData(Context context, Activity activity){
        this.activity = activity;
        DateBaseHelper dateBaseHelper = new DateBaseHelper(context, "weight_db",
                null, 1);
        database = dateBaseHelper.getReadableDatabase();
    }

    private float[] getData(){
        Cursor cursor = database.query("user", null, null, null,
                null, null, "id desc");
        int num = 0;
        int totalNum = 10;
        //float[] arr = new float[10];
        float[] arr = new float[10];

        while(cursor.moveToNext() && num < totalNum){
            float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
            arr[num] = weight;
            num++;
        }
        cursor.close();
        return arr;
    }

    public void show(){
        float[] arr = getData();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putFloatArray("arr", arr);
        intent.putExtras(bundle);
        intent.setClass(activity, DatabaseShowActivity.class);
        activity.startActivity(intent);
    }

}
