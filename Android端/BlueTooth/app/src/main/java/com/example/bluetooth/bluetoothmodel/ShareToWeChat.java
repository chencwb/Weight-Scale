package com.example.bluetooth.bluetoothmodel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.example.bluetooth.supportutils.CollectControl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author chenweibin
 * @date 2019-12-31 19:29:32
 */
public class ShareToWeChat {
    private Activity activity;

    public ShareToWeChat(Activity activity)
    {
        this.activity = activity;
    }

    public void copyMessage(){
        CollectControl collectControl = new CollectControl();
        collectControl.initControl(activity);
        String weightTv = (String) collectControl.getWeightText().getText();
        String bmiTv = (String) collectControl.getBmiShowText().getText();
        Editable heightTv = collectControl.getHeightText().getText();
        String bmiSuggestionTv = (String) collectControl.getBmiSuggestion().getText();
        String compareWeightTv = (String) collectControl.getCompareText().getText();

        //get local time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(new Date());

        String totalShareString = date + "\n" + "体重："+ weightTv + "\n" +
                "身高：" + heightTv + "\n" +
                bmiTv + "\n" + bmiSuggestionTv + "\n" +
                compareWeightTv;

        //剪贴板
        ClipboardManager copy = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        //数据
        ClipData clipData = ClipData.newPlainText(null, totalShareString);
        copy.setPrimaryClip(clipData);

        Toast.makeText(activity, "信息已复制到剪切板", Toast.LENGTH_SHORT).show();
        intentToWeChat();
    }

    private void intentToWeChat(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName componentName = new ComponentName("com.tencent.mm",
                    "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(componentName);
            activity.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(activity, "检查到您手机没有安装微信，请安装后使用该功能",
                    Toast.LENGTH_SHORT).show();
            Log.e("ShareToWeChat", e.toString());
        }
    }


}
