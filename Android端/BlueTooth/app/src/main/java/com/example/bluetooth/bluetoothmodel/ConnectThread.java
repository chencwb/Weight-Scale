package com.example.bluetooth.bluetoothmodel;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetooth.R;
import com.example.bluetooth.supportutils.CollectControl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import static com.example.bluetooth.MainActivity.bluetoothAdapter;
import static com.example.bluetooth.MainActivity.bluetoothDevice;
import static com.example.bluetooth.MainActivity.socket;

/**
 * @author gd_ch
 * @date 2019-12-28
 */
public class ConnectThread extends Thread {

    private Activity activity;
    private Context context;

    ConnectThread(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        BluetoothSocket tmp = null;
        try {
            assert bluetoothDevice != null;
            String myUuid = "00001101-0000-1000-8000-00805F9B34FB";
            tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(myUuid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = tmp;
    }

    @Override
    public void run() {
        bluetoothAdapter.cancelDiscovery();
        try {
            socket.connect();
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"蓝牙已连接", Toast.LENGTH_SHORT).show();
                    //set connect status
                    CollectControl collectControl = new CollectControl();
                    collectControl.initControl(activity);
                    TextView textView = collectControl.getConnectStatusText();
                    textView.setText("蓝牙已连接");

                    //连接上蓝牙时按钮改绿色
                    Button connectBtn = collectControl.getConnectBtn();
                    connectBtn.setBackgroundResource(R.drawable.bg_connect);
                }
            });

        } catch (IOException e) {
            final String tag = "ConnectThread";
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "蓝牙连接失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            });
            Log.e(tag, e.toString());
            try {
                Method m = bluetoothDevice.getClass().
                        getMethod("createRfcommSocket", int.class);
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                socket.connect();
            }catch (Exception ee){
                Log.e(tag, ee.toString());
                try{
                    socket.close();
                }catch (Exception e1){
                    Log.e(tag, e1.toString());
                }
            }

        }
            GetMessage getMessage = new GetMessage(activity, context);
            getMessage.start();

    }

}

