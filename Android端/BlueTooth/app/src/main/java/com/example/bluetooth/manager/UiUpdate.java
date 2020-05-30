package com.example.bluetooth.manager;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.bluetooth.supportutils.CollectControl;

import java.text.DecimalFormat;

/**
 * @author chenweibin
 * @date 2019-12-29
 */
public class UiUpdate {
    private Activity activity;

    public UiUpdate(Activity activity){
        this.activity = activity;
    }

    public void updateWeight(final double a){
        //must in the main thread to update ui
        //change to main thread
        //update weight
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                CollectControl collectControl = new CollectControl();
                collectControl.initControl(activity);
                //get weightText control
                TextView weightText = collectControl.getWeightText();
                //change double to string
                DecimalFormat format = new DecimalFormat("00.00");
                String weight = format.format(a);
                if(a == 0){
                    //weight has no value
                    weightText.setText("--.--");
                    TextView bmi = collectControl.getBmiShowText();
                    bmi.setText("--.-");
                }else{
                    //show weight to ui
                    weightText.setText(weight);
                }

            }
        });
    }

    void showPastData(double pastWeight, double avgWeight){
        if(pastWeight != 0 && avgWeight != 0)
        {
            CollectControl collectControl = new CollectControl();
            collectControl.initControl(activity);
            TextView compareWightText = collectControl.getCompareText();
            String show = "上次测量体重为"+pastWeight+"Kg"+"\n"+"以往平均体重为"+avgWeight+"Kg";
            compareWightText.setText(show);
        }
    }
}
