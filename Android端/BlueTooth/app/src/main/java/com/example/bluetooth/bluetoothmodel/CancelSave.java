package com.example.bluetooth.bluetoothmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bluetooth.db.DateBaseHelper;

/**
 * @author chenweibin
 * @date 2020-1-5 16:05:47
 */
public class CancelSave {
    private Context context;

    public CancelSave(Context context) {
        this.context = context;
    }

    public void noSave(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("weightData", Context.MODE_PRIVATE);
        double pastAvg = sharedPreferences.getFloat("avgWeight", (float) 00.00);
        int num = sharedPreferences.getInt("times", 0);
        double pastWeight = sharedPreferences.getFloat("pastWeight", (float) 00.00);
        double pastPastWeight = sharedPreferences.getFloat("pastPastWeight", (float) 00.00);
        double avg = (pastAvg * num - pastWeight) / ( --num );

        //get sharePreferences name is "weightData"
        SharedPreferences.Editor editor = context.getSharedPreferences("weightData",
                Context.MODE_PRIVATE).edit();
        editor.putFloat("pastWeight", (float) pastPastWeight);
        editor.putFloat("avgWeight", (float) avg);
        editor.putInt("times", num);
        editor.apply();

        //delete
        DateBaseHelper dateBaseHelper = new DateBaseHelper(context, "weight_db",
                null, 1);
        SQLiteDatabase database = dateBaseHelper.getReadableDatabase();
        Cursor cursor = database.query("user", null,null,null,
                null, null, "ID desc");
        if(cursor.moveToNext()){
            int idNum = cursor.getInt(cursor.getColumnIndex("id"));
            String sql = "delete from user where id = " + idNum;
            database.execSQL(sql);
            cursor.close();
        }
    }
}

