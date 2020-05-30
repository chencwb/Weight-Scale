package com.example.bluetooth.manager;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bluetooth.bluetoothmodel.CalculateBmi;
import com.example.bluetooth.bluetoothmodel.CancelSave;
import com.example.bluetooth.bluetoothmodel.DbQueryGetData;
import com.example.bluetooth.bluetoothmodel.GetMessage;
import com.example.bluetooth.bluetoothmodel.ShareToWeChat;
import com.example.bluetooth.bluetoothmodel.ToConnectBluetooth;
import com.example.bluetooth.supportutils.CollectControl;
import com.example.bluetooth.supportutils.OpenBluetooth;
import com.example.bluetooth.supportutils.Vibrate;

import static com.example.bluetooth.MainActivity.socket;

/**
 * @author chenweibin
 * @date 2019-12-28
 */
public class BluetoothManager{
    private Activity activity;
    private Context context;

    public BluetoothManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void onClick(int id){
        //震动
        Vibrate.vibrator(context);
        switch (id){
            case 1:
                //flag为1时 表示打开蓝牙成功
                OpenBluetooth openBluetooth = new OpenBluetooth(activity);
                int flag = openBluetooth.openTheBluetooth();
                if(flag == 1){
                    //open bluetooth success
                    ToConnectBluetooth toConnectBluetooth = new ToConnectBluetooth(activity, context);
                    toConnectBluetooth.connectToTheBluetooth();
                }else{
                    //open bluetooth fail
                    Toast.makeText(context, "蓝牙打开失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                //不保存
                if(socket != null){
                    GetMessage getMessage = new GetMessage(activity ,context);
                    boolean hasSave =  getMessage.getHasSaved();
                    if(hasSave){
                        CancelSave cancelSave = new CancelSave(context);
                        cancelSave.noSave();
                        Toast.makeText(context, "本次保存数据已撤销", Toast.LENGTH_SHORT).show();
                        getMessage.setHasSaved(false, false);
                    }else{
                        Toast.makeText(context, "本次数据未保存，无须撤销数据", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "蓝牙未连接", Toast.LENGTH_SHORT).show();
                }

                break;


            case 3:
                ShareToWeChat shareToWechat = new ShareToWeChat(activity);
                shareToWechat.copyMessage();
                break;

                //height sure button
            case 4:
                CollectControl collectControl = new CollectControl();
                collectControl.initControl(activity);
                //height input, clear focus to hide softInput
                EditText editText = collectControl.getHeightText();
                editText.clearFocus();
                //if click, calculate bmi
                CalculateBmi calculateBmi = new CalculateBmi(activity);
                double bmi = calculateBmi.bmi();
                if(bmi == 0){
                    //height or weight contain "-"
                    break;
                }
                //bmi has value, show bmi
                BmiNotice bmiNotice = new BmiNotice(bmi, activity);
                bmiNotice.showBmi();
                break;

            case 5:
                DbQueryGetData dbQueryGetData = new DbQueryGetData(context,activity);
                dbQueryGetData.show();
                break;

            default:
                break;
        }
    }

}
