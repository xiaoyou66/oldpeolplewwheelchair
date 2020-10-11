package com.example.oldpeople.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.oldpeople.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChartFragment extends Fragment{
    View view;
    @BindView(R.id.chart) LineChart chart;
    private FragmentTransaction fragmentTransaction;
    private String api = "http://122.51.134.241:1324/api/v1/get/recent_heart";
    private ReturnData data;

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
//        QMUITabBuilder tabBuilder = mTabSegment.tabBuilder().setGravity(Gravity.CENTER);
//        int indicatorHeight = QMUIDisplayHelper.dp2px(view.getContext(), 2);
//        mTabSegment.reset();
//        mTabSegment.setIndicator(new QMUITabIndicator(indicatorHeight, false, true));
//        mTabSegment.addTab(tabBuilder.setText("心率统计").build(getContext()));
//        mTabSegment.addTab(tabBuilder.setText("血压统计").build(getContext()));
//        mTabSegment.notifyDataChanged();
//        mTabSegment.selectTab(0);
//        mTabSegment.setOnTabClickListener((int index)->{
//            Log.e("你选择了",index+"");
//        });
        // 获取数据
        getRecent();
    }

    private void ShowHeart(){
        // 判断数据是否为空
        if (data.data.isEmpty()){
            return;
        }
        // programmatically create a LineChart
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> entries2 = new ArrayList<Entry>();
        List<Entry> entries3 = new ArrayList<Entry>();

        for(int i=0;i<data.data.size();i++){
            Data d = data.data.get(i);
            entries.add(new Entry(i,d.heart));
            entries2.add(new Entry(i,d.l_pressure));
            entries3.add(new Entry(i,d.h_pressure));
        }

        LineDataSet dataSet = new LineDataSet(entries, "心率统计");
        dataSet.setColor(R.color.red);
        dataSet.setValueTextColor(R.color.primary);
        /*高亮*/
        LineData lineData = new LineData(dataSet);
        lineData.addDataSet(new LineDataSet(entries2, "收缩压"));
        lineData.addDataSet(new LineDataSet(entries3, "舒张压"));
        chart.setData(lineData);
        /*上面这些是设置数据*/

        /*动画效果*/
        chart.animateXY(1000,1000);
        /*图像圆润*/
        chart.invalidate();
    }

    // 数据初始化
    private void InitData(){

    }

    // 获取统计信息
    private void getRecent(){
        // 执行线程获取GPS坐标信息
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(api).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = handler.obtainMessage();
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    Gson gson = new Gson();
                    String json= Objects.requireNonNull(response.body()).string();
                    // 打印获取到的网络数据
                    Log.e("xiaoyou",json);
                    // 因为转换过程中会出现错误信息，所以需要进行错误处理
                    try {
                        // 对获取到的数据进行解析转换为对象
                        data = gson.fromJson(json, ReturnData.class);
                        // 显示数据的最后更新时间
                        //线程通信通知
                        msg.what = 1;
                    } catch (Exception e){
                        // what为5 说明更新数据失败
                        msg.what = 2;
                    }
                } else {
                    msg.what = 2;
                }
                handler.sendMessage(msg);
            }
        });
    }


    // 返回的data数据
    public static class ReturnData{
        public int code;
        public String msg;
        public List<Data> data;
    }

    // json解析数据
    public static class Data{
        public int heart;
        public int h_pressure;
        public int l_pressure;
        public String date;
    }

    //线程和主线程发送消息
    Looper looper=Looper.myLooper();
    //接收消息与处理消息
    private Handler handler =new Handler(looper) {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            //更新UI组件
            switch (msg.what){
                case 1:
                    // 请求执行成功
                    Toast.makeText(view.getContext(),"获取最近信息成功!",Toast.LENGTH_SHORT).show();
                    ShowHeart();
                    break;
                case 2:
                    //  请求执行失败
                    Toast.makeText(view.getContext(),"获取最近信息失败!",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
