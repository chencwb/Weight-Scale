package com.example.bluetooth.bluetoothmodel;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import com.example.bluetooth.manager.BmiNotice;
import com.example.bluetooth.manager.UiUpdate;
import com.example.bluetooth.supportutils.AbsUtil;
import com.example.bluetooth.supportutils.ChangeFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


import static com.example.bluetooth.MainActivity.socket;

/**
 * @author chenweibin
 * @date 2019-12-28
 */
public class GetMessage extends Thread {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Activity activity;
    private Context context;

    private static boolean hasSaved = false;
    private double[] weightArray = {0, 0, 0};
    private int index = 0;

    /**
     * 数据保存之后ui不再变化， 数据为0时清除该标志
     */
    private boolean uiNoChange = false;
    /**
     * 第几次接收到数据
     */
    private int i = 0;

    public GetMessage(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        //单片机->手机
        InputStream input = null;
        //手机->单片机
        OutputStream output = null;

        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.inputStream = input;
        this.outputStream = output;
    }


    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                //从输入流读取一定数量的字节存在buff数组中，返回读取的字节数
                bytes = inputStream.read(buffer);
                String str = new String(buffer, StandardCharsets.ISO_8859_1);
                str = str.substring(0, bytes);

                //获取第一个字符
                char[] ch = str.toCharArray();
                int a = ChangeFormat.asciiToInt(ch[0]);

                bytes = inputStream.read(buffer);
                str = new String(buffer, StandardCharsets.ISO_8859_1);
                str = str.substring(0, bytes);

                //获取第二个字符
                ch = str.toCharArray();
                int b = ChangeFormat.asciiToInt(ch[0]);

                double c;
                if(a>b){
                    c = a % 100 + 0.01 * b;
                }else{
                    c = b % 100 + 0.01 * a;
                }

                if(c == 0){
                    hasSaved = false;
                }

                i++;
                if(!hasSaved){
                    if(i >= 5){
                        weightArray[index++] = c;
                        if(index == 3){
                            //刚好取三个值
                            if( AbsUtil.abs(weightArray[0], weightArray[1]) < 0.5 &&
                                    AbsUtil.abs(weightArray[1], weightArray[2]) < 0.5 &&
                                    AbsUtil.abs(weightArray[0], weightArray[2]) < 0.5 ){
                                //保存  avgWeight
                                double saveWeightData =
                                        (weightArray[0] + weightArray[1] + weightArray[2]) / 3;
                                if(saveWeightData != 0){
                                    //weight value only save when no zero
                                    SaveData saveData1 = new SaveData(context, saveWeightData);
                                    saveData1.saveData();
                                    hasSaved = true;
                                    uiNoChange = true;
                                    UiUpdate uiUpdate = new UiUpdate(activity);
                                    uiUpdate.updateWeight(saveWeightData);
                                }else{
                                    uiNoChange = false;
                                }
                            }
                            index = 0;
                        }
                    }
                }
                //更新UI
                if(!uiNoChange){
                    UiUpdate uiUpdate = new UiUpdate(activity);
                    uiUpdate.updateWeight(c);
                    //update bmi
                    CalculateBmi calculateBmi = new CalculateBmi(activity);
                    double bmi = calculateBmi.bmi();
                    if(bmi != 0){
                        final double bmiBmi = bmi;
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                BmiNotice bmiNotice = new BmiNotice(bmiBmi, activity);
                                bmiNotice.showBmi();
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(byte bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getHasSaved(){
        return hasSaved;
    }

    public void setHasSaved(boolean status, boolean ui){
        hasSaved = status;
        uiNoChange = ui;
    }

}


