package com.example.oldpeople.fragment;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.oldpeople.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapFragment extends Fragment{
    private String api ="http://122.51.134.241:1324/api/v1/get/gps";
    private View view;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.last_update)
    TextView last;
    @BindView(R.id.refresh)
    TextView refresh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_map,container,false);
        //使用黄油刀
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 刷新按钮点击事件
        refresh.setOnClickListener(v->{
            getPosition();
        });
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        AMap aMap = null;
        aMap = mapView.getMap();
        MyLocationStyle myLocationStyle;
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        //显示比例尺
        //定义一个UiSettings对象
        UiSettings mUiSettings;
        //实例化UiSettings类对象
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setScaleControlsEnabled(true);
        //设置默认显示坐标,同时标记当前的位置
        setPosition(28.254653,114.777046);
        getPosition();
    }
    private void getPosition(){
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
                        ReturnData res = gson.fromJson(json,ReturnData.class);
                        // 显示数据的最后更新时间
                        Message msg2 = handler.obtainMessage();
                        msg2.what = 3;
                        msg2.obj = res.data.n+","+res.data.e+","+res.data.date;
                        handler.sendMessage(msg2);
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

    // 设置地图的坐标
    private void setPosition(double gpsN,double gpsE){
        AMap aMap = mapView.getMap();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsN,gpsE), 19));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(gpsN,gpsE));
        markerOptions.title(last.getText().toString());
        markerOptions.visible(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_on));
        markerOptions.icon(bitmapDescriptor);
        aMap.addMarker(markerOptions);
    }

    // 返回的data数据
    public static class ReturnData{
        public int code;
        public String msg;
        public Data data;
    }

    // json解析数据
    public static class Data{
        public String n;
        public String e;
        public String date;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
                    Toast.makeText(view.getContext(),"获取位置成功!",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //  请求执行失败
                    Toast.makeText(view.getContext(),"获取位置失败!",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    String[] data = msg.obj.toString().split(",");
                    // 显示地图数据
                    setPosition(Double.parseDouble(data[0]),Double.parseDouble(data[1]));
                    // 显示最后更新时间
                    last.setText("最后更新时间:"+data[2]);
                    // 获取到了数据
                    Log.e("xiaoyou",msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
}
