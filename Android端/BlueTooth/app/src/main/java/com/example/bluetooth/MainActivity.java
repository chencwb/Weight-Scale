package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bluetooth.manager.BluetoothManager;
import com.example.bluetooth.manager.WhenOpenReadPastData;
import com.example.bluetooth.supportutils.Vibrate;

import java.io.IOException;


/**
 * @author chenweibin
 * @date 2019-12-27
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public BluetoothManager bluetoothManager = null;
    /**
     * 计算两次返回时时间间隔
     */
    private long exitTime = 0;

    Button connectBtn;
    EditText heightText;
    Button heightSure;
    ImageView imageView;
    ImageView noSaveImageView;
    ImageView dataBaseShow;

    public static BluetoothDevice bluetoothDevice = null;
    public static BluetoothAdapter bluetoothAdapter = null;
    public static BluetoothSocket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //第一次启动时加载以往体重和平均体重
        WhenOpenReadPastData whenOpenReadPastData =
                new WhenOpenReadPastData(this,this);
        whenOpenReadPastData.firstSetPastWeight();

        //实例化manager
        bluetoothManager = new BluetoothManager(this, this);

        //连接蓝牙
        connectBtn = findViewById(R.id.conntect_btn);
        connectBtn.setOnClickListener(this);

        imageView = findViewById(R.id.weChatBtn);
        imageView.setOnClickListener(this);

        noSaveImageView = findViewById(R.id.noSaveBtn);
        noSaveImageView.setOnClickListener(this);

        dataBaseShow = findViewById(R.id.quxian);
        dataBaseShow.setOnClickListener(this);


        //身高编辑框
        heightText = findViewById(R.id.height_text);
        heightText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager manager = ((InputMethodManager)getSystemService
                        (Context.INPUT_METHOD_SERVICE));

                if(hasFocus){//获得焦点
                    //震动
                    Vibrate.vibrator(MainActivity.this);
                    //文本清空
                    heightText.setText("");
                    //开软键盘
                    if(manager != null){
                        manager.showSoftInput(v,0);
                    }
                }
                else{//失去焦点
                    if(manager != null){
                        //收起键盘
                        manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
            }
        });
        //身高确定框
        heightSure = findViewById(R.id.height_btn);
        heightSure.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("MainActivity", e.toString());
            }
        }
        //退出时关闭蓝牙
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击连接蓝牙
            case R.id.conntect_btn:
                bluetoothManager.onClick(1);
                break;

            case R.id.noSaveBtn:
                bluetoothManager.onClick(2);
                break;

            case R.id.weChatBtn:
                bluetoothManager.onClick(3);
                break;

            //身高确定按钮
            case R.id.height_btn:
                bluetoothManager.onClick(4);
                break;

            case R.id.quxian:
                //Intent intent = new Intent(MainActivity.this, DatabaseShowActivity.class);
                //startActivity(intent);
                bluetoothManager.onClick(5);
                break;

             default:
                 break;
        }
    }

    /**
     *按返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 计算时间间隔
     */
    private void exit(){
        final int time = 2000;
        if(System.currentTimeMillis() - exitTime > time){
            Toast.makeText(this, "再次返回退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            finish();
        }
    }
}
