package com.example.videoplayer.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.videoplayer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartFragment extends Fragment{
    View view;
    @BindView(R.id.tabSegment) QMUITabSegment mTabSegment;
    @BindView(R.id.chart) LineChart chart;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chart,container,false);
        ButterKnife.bind(this, view);
        //使用黄油刀
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置内容
        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder().setGravity(Gravity.CENTER);
        int indicatorHeight = QMUIDisplayHelper.dp2px(view.getContext(), 2);
        mTabSegment.reset();
        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
        mTabSegment.addTab(tabBuilder.setText("心率统计").build(getContext()));
        mTabSegment.addTab(tabBuilder.setText("血压统计").build(getContext()));
        mTabSegment.notifyDataChanged();
        mTabSegment.selectTab(0);
        mTabSegment.setOnTabClickListener((int index)->{
            Log.e("你选择了",index+"");
        });
        ShowHeart();
    }

    private void ShowHeart(){
        // programmatically create a LineChart
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> entries2 = new ArrayList<Entry>();

        entries.add(new Entry(1,10));
        entries.add(new Entry(2,8));
        entries.add(new Entry(3,20));
        entries.add(new Entry(4,20));
        entries.add(new Entry(5,20));

        entries2.add(new Entry(1,30));
        entries2.add(new Entry(2,1));
        entries2.add(new Entry(3,5));
        entries2.add(new Entry(4,20));
        entries2.add(new Entry(5,10));
        LineDataSet dataSet = new LineDataSet(entries, "心率统计");
        dataSet.setColor(R.color.red);
        dataSet.setValueTextColor(R.color.primary);
        /*高亮*/
        LineData lineData = new LineData(dataSet);
        lineData.addDataSet(new LineDataSet(entries2, "血压"));
        chart.setData(lineData);
        /*上面这些是设置数据*/

        /*动画效果*/
        chart.animateXY(1000,1000);
        /*图像圆润*/
        chart.invalidate();
    }

}
