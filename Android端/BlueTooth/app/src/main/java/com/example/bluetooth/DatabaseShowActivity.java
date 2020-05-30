package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.Objects;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenweibin
 * @date 2020-1-6 09:25:27
 * MpChart显示数据
 */
public class DatabaseShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_show);

        boolean showFlag = false;

        //get intent data
        float[] arr = Objects.requireNonNull(this.getIntent().getExtras()).getFloatArray("arr");

        List<Entry> entries = new ArrayList<>();

        assert arr != null;
        int max = arr.length;
        int index = 1;
        int allHaveData = 11;
        for (int i=max-1; i >= 0; i--) {
            if(arr[i] != 0.0){
                showFlag = true;
                entries.add(new Entry(index++, arr[i]));
            }
        }

        if(showFlag) {
            //实例化
            LineChart lineChart = findViewById(R.id.lineChart);
            LineDataSet dataSet;
            if(index == allHaveData){
                //有10次数据
                dataSet = new LineDataSet(entries, "Weight  最近10次数据");
            }else{
                //不满10次数据
                dataSet = new LineDataSet(entries, "Weight  显示全部数据");
            }
            //线条颜色
            dataSet.setColor(Color.parseColor("#7d7d7d"));
            //圆点颜色
            dataSet.setCircleColor(Color.parseColor("#7d7d7d"));
            //线条宽度
            dataSet.setLineWidth(2f);

            //设置样式,右边y轴
            YAxis rightAxis = lineChart.getAxisRight();
            //设置图表右边的y轴禁用
            rightAxis.setEnabled(false);

            //左边y轴
            YAxis leftAxis = lineChart.getAxisLeft();
            //设置图表左边的y轴禁用
            leftAxis.setEnabled(false);

            //设置x轴
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setTextColor(Color.parseColor("#333333"));
            xAxis.setTextSize(11f);
            xAxis.setAxisMinimum(0f);

            //是否绘制轴线
            xAxis.setDrawAxisLine(true);
            //设置x轴上每个点对应的线
            xAxis.setDrawGridLines(false);
            //绘制标签  指x轴上的对应数值
            xAxis.setDrawLabels(true);
            //设置x轴的显示位置
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //禁止放大后x轴标签重绘
            xAxis.setGranularity(1f);

            //隐藏x轴描述
            Description description = new Description();
            description.setEnabled(false);
            lineChart.setDescription(description);

            //chart设置数据
            LineData lineData = new LineData(dataSet);
            //是否绘制线条上的文字
            lineData.setDrawValues(true);
            lineData.setValueTextSize(13);
            lineChart.setData(lineData);
            // refresh
            lineChart.invalidate();
        }
    }
}
