package com.example.bluetooth.manager;

import android.app.Activity;
import android.content.Context;

import com.example.bluetooth.supportutils.ReadPastData;

/**
 * @author chenweibin
 * @date 2019-12-29
 */
public class WhenOpenReadPastData {
    private Context context;
    private Activity activity;

    public WhenOpenReadPastData(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void firstSetPastWeight(){
        ReadPastData readPastData = new ReadPastData(context);
        //get past weight value
        double pastWeight = readPastData.pastWeightData();
        //get past avgWeight value
        double avgWeight = readPastData.avgWeight();
        UiUpdate uiUpdate = new UiUpdate(activity);
        //show past weight and avgWeight in view
        uiUpdate.showPastData(pastWeight,avgWeight);
    }

}
