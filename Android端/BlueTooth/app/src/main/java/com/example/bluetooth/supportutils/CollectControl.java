package com.example.bluetooth.supportutils;


import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bluetooth.R;


/**
 * @author chenweibin
 * @date 2019-12-27
 */
public class CollectControl {
    private Button connectBtn;
    private TextView connectStatusText;
    private TextView weightText;
    private EditText heightText;
    private TextView bmiShowText;
    private TextView bmiSuggestion;
    private TextView compareText;


    /**
     * 注册控件
     */
    public void initControl(Activity activity){
        //connect button
        connectBtn = activity.findViewById(R.id.conntect_btn);
        //connect status text
        connectStatusText = activity.findViewById(R.id.bluetoothStatus_text);
        //show weight text
        weightText = activity.findViewById(R.id.weight_text);
        //height editText
        heightText = activity.findViewById(R.id.height_text);
        //show bmi text
        bmiShowText = activity.findViewById(R.id.shouBmi_text);
        //show bmi suggestion
        bmiSuggestion = activity.findViewById(R.id.bmiSuggestion);
        //show past weight and avgWeight
        compareText = activity.findViewById(R.id.compareWeight_text);

    }


    public TextView getConnectStatusText(){
        return connectStatusText;
    }

    public Button getConnectBtn(){
        return connectBtn;
    }

    public TextView getWeightText(){
        return weightText;
    }

    public EditText getHeightText(){
        return heightText;
    }

    public TextView getBmiShowText(){
        return bmiShowText;
    }

    public TextView getBmiSuggestion(){
        return bmiSuggestion;
    }

    public TextView getCompareText(){
        return compareText;
    }

}
