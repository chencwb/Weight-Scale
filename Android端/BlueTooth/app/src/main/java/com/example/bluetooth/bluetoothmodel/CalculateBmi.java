package com.example.bluetooth.bluetoothmodel;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bluetooth.supportutils.ChangeFormat;
import com.example.bluetooth.supportutils.CollectControl;

/**
 * @author gd_ch
 * @date 2019-12-29
 */
public class CalculateBmi {
    private Activity activity;

    public CalculateBmi(Activity activity){
        this.activity = activity;
    }

    public double bmi(){
        final String illegalString = "-";
        CollectControl collectControl = new CollectControl();
        collectControl.initControl(activity);
        EditText editText = collectControl.getHeightText();
        //heightText
        String tmpHeight = editText.getText().toString();
        if(tmpHeight.contains(illegalString) || tmpHeight.length()==0 ){
            return 0;
        }
        //get height value
        double height = ChangeFormat.stringToDouble(tmpHeight);
        TextView textView = collectControl.getWeightText();
        String tmpWeight = textView.getText().toString();
        if(tmpWeight.contains(illegalString)){
            return 0;
        }
        //get weight value
        double weight = ChangeFormat.stringToDouble(tmpWeight);
        //返回bmi
        return weight / (height/100) / (height/100);
    }
}
