package com.example.bluetooth.manager;

import android.app.Activity;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.supportutils.CollectControl;

import java.text.DecimalFormat;

/**
 * @author chenweibin
 * @date 2019-12-29
 */
public class BmiNotice {
    private double bmi;
    private Activity activity;

    public BmiNotice(double bmi, Activity activity){
        this.bmi = bmi;
        this.activity = activity;
    }

    public void showBmi(){
        final double bmiValue1 = 18.4;
        final double bmiValue2 = 24;
        final double bmiValue3 = 28;

        //double to string
        DecimalFormat format = new DecimalFormat("00.0");
        String strBmi = format.format(bmi);
        String total = "BMI: "+strBmi;
        CollectControl collectControl = new CollectControl();
        collectControl.initControl(activity);
        TextView textView = collectControl.getBmiShowText();
        //show bmi
        textView.setText(total);
        TextView bmiSuggestion = collectControl.getBmiSuggestion();
        if(bmi <= bmiValue1){
            //偏瘦
            bmiSuggestion.setText(R.string.bmiSuggestion1);
        }else if(bmi < bmiValue2){
            //正常
            bmiSuggestion.setText(R.string.bmiSuggestion2);
        }else if(bmi < bmiValue3){
            //过重
            bmiSuggestion.setText(R.string.bmiSuggestion3);
        }else{
            //肥胖
            bmiSuggestion.setText(R.string.bmiSuggestion4);
        }
    }

}
