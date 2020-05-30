package com.example.bluetooth.supportutils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static com.example.bluetooth.MainActivity.bluetoothAdapter;

/**
 * open or connect the bluetooth
 * @author chenweibin
 * @date 2019-12-27
 */
public class OpenBluetooth {
    private Activity activity;

    public OpenBluetooth(Activity activity){
        this.activity = activity;
    }

    public int openTheBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            //not support bluetooth
            Log.w("OpenBluetooth","手机不支持蓝牙退出");
            return 0;
        }

        if(!bluetoothAdapter.isEnabled()){
            //蓝牙未打开
            Toast.makeText(activity,"蓝牙未开启，正在打开", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(intent);
            Toast.makeText(activity,"蓝牙已打开", Toast.LENGTH_SHORT).show();
        }else{
            //蓝牙已打开
            Toast.makeText(activity,"蓝牙已打开", Toast.LENGTH_SHORT).show();
        }
        return 1;
    }

}
