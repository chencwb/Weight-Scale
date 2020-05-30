package com.example.bluetooth.supportutils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author chenweibin
 * @date 2019-12-29
 */
public class ReadPastData {
    private SharedPreferences sharedPreferences;

    public ReadPastData(Context context){
        sharedPreferences = context.getSharedPreferences("weightData", Context.MODE_PRIVATE);
    }

    public double pastWeightData(){
        double weight;
        //如果取不到体重就取00.0
        weight = sharedPreferences.getFloat("pastWeight", (float) 00.00);
        //格式转换保留2位小数
        weight = (double)Math.round(weight*100) / 100;
        return weight;
    }

    public double avgWeight(){
        double avg;
        //如果取不到就取00.0
        avg = sharedPreferences.getFloat("avgWeight", (float) 00.00);
        //格式转换保留2位小数
        avg = (double)Math.round(avg*100) / 100;
        return avg;
    }

    public int times(){
        int num;
        //取不到次数就取0
        num = sharedPreferences.getInt("times", 0);
        return num;
    }

}
