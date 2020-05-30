package com.example.bluetooth.bluetoothmodel;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.example.bluetooth.db.DateBaseHelper;
import com.example.bluetooth.supportutils.ReadPastData;

/**
 * @author chenweibin
 * @date 2019-12-29
 */
class SaveData {
    private Context context;
    private double lastWeight;
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase db;
    SaveData(Context context, double a){
        this.context = context;
        this.lastWeight = a;
        sharedPreferences = context.getSharedPreferences("weightData", Context.MODE_PRIVATE);
        DateBaseHelper datebaseHelper = new DateBaseHelper(context, "weight_db",
                null, 1);
        db = datebaseHelper.getReadableDatabase();
    }

    void saveData(){
        //get sharePreferences name is "weightData"
        SharedPreferences.Editor editor = context.getSharedPreferences("weightData",
                Context.MODE_PRIVATE).edit();

        ReadPastData readPastData = new ReadPastData(context);
        //times为0表示第一次测量，直接存储
        int times = readPastData.times();
        double avgWeight = readPastData.avgWeight();

        if(times == 0){
            //直接存储
            editor.putFloat("pastWeight", (float) lastWeight);
            editor.putFloat("pastPastWeight", (float) 00.00);
            editor.putFloat("avgWeight", (float) lastWeight);
            editor.putInt("times", 1);
        }else{
            //重新计算times和avg
            avgWeight = (times * avgWeight + lastWeight) / (times +1);
            times += 1;
            double pastWeight = sharedPreferences.getFloat("pastWeight", (float) 00.00);
            editor.putFloat("pastPastWeight", (float)pastWeight);
            editor.putFloat("pastWeight", (float) lastWeight);
            editor.putFloat("avgWeight", (float) avgWeight);
            editor.putInt("times", times);
        }
        editor.apply();

        //insert value
        ContentValues contentValues = new ContentValues();
        contentValues.put("weight", lastWeight);
        db.insert("user", null, contentValues);
    }

}
