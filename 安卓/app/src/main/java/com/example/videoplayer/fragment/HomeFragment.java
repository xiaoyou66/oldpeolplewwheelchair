package com.example.videoplayer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.videoplayer.R;
import com.example.videoplayer.video.MyPlayer;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.androidhive.fontawesome.FontDrawable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.videoplayer.tool.Tools.GetData;
import static com.example.videoplayer.tool.Tools.SetData;

public class HomeFragment extends Fragment {
    private MyPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private QMUIPullRefreshLayout refersh;
    private String api ="http://122.51.134.241:1324/api/v1/get/heart";
    private String url="";
    View view;
    @BindView(R.id.text_heart)
    TextView text_heart;
    @BindView(R.id.font_heart)
    ImageButton img_heart;
    @BindView(R.id.progress_heart)
    RoundCornerProgressBar ps_heart;
    @BindView(R.id.text_pause_up)
    TextView text_pauseup;
    @BindView(R.id.font_pause_up)
    ImageButton img_pauseup;
    @BindView(R.id.progress_preasureup)
    RoundCornerProgressBar ps_pauseup;
    @BindView(R.id.text_pause_down)
    TextView text_pausedown;
    @BindView(R.id.font_pause_down)
    ImageButton img_passdown;
    @BindView(R.id.progress_preasuredown)
    RoundCornerProgressBar ps_pausedown;
    @BindView(R.id.last_update)
    TextView last_update;
    @BindView(R.id.rtsp_adder)
    TextView r_adder;
    @BindView(R.id.reset_adder)
    TextView reset_adder;
    @BindView(R.id.refresh_video)
    TextView refresh_video;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        //使用黄油刀
        return view;
    }

    @SuppressLint("ShowToast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置按钮的点击事件
        reset_adder.setOnClickListener(v -> {
            // 清除数据
            SetData(view.getContext(),"rtsp","");
            // 继续初始化
            init(view);
        });
        refresh_video.setOnClickListener(v->{
            if(videoPlayer!=null){
                videoPlayer.setUp(url, false, "");
                videoPlayer.startPlayLogic();
                Toast.makeText(view.getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        // 设置刷新按钮的点击事件
        init(view);
        heart();
        preasure();
        preasuredown();
    }

    //心率显示控制
    private void heart() {
        // using paper plane icon for FAB
        FontDrawable drawable = new FontDrawable(view.getContext(), R.string.fa_heartbeat_solid, true, false);
        // white color to icon
        drawable.setTextColor(ContextCompat.getColor(view.getContext(), android.R.color.holo_red_dark));
        drawable.setTextSize(36);
        img_heart.setImageDrawable(drawable);
    }

    //收缩压显示控制
    private void preasure() {
        FontDrawable drawable = new FontDrawable(view.getContext(), R.string.fa_prescription_bottle_alt_solid, true, false);
        // white color to icon
        drawable.setTextColor(ContextCompat.getColor(view.getContext(), R.color.warning));
        drawable.setTextSize(36);
        img_pauseup.setImageDrawable(drawable);
    }

    //舒张压显示控制
    private void preasuredown(){
        FontDrawable drawable = new FontDrawable(view.getContext(), R.string.fa_prescription_bottle_solid, true, false);
        // white color to icon
        drawable.setTextColor(ContextCompat.getColor(view.getContext(),R.color.primary));
        drawable.setTextSize(36);
        img_passdown.setImageDrawable(drawable);
    }

    //线程对象
    public class RThread implements Runnable {
        @Override
        public void run() {
            String data=Thread.currentThread().getName();
            String datas[]=data.split(",");
            for (int i = 0; i <Integer.parseInt(datas[0]); i++) {
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what =Integer.parseInt(datas[1]);
                msg.obj = i;
                handler.sendMessage(msg);
            }
            Message msg = handler.obtainMessage();
            msg.what =Integer.parseInt(datas[1]);
            msg.obj=datas[0];
            handler.sendMessage(msg);
        }
    }


    //线程和主线程发送消息
    Looper looper=Looper.myLooper();
    //接收消息与处理消息
    private Handler handler =new Handler(looper) {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            //更新UI组件
            switch (msg.what) {
                case 1:
                    ps_heart.setProgress(Float.parseFloat(String.valueOf(msg.obj)));
                    text_heart.setText(String.valueOf(msg.obj));
                    break;
                case 2:
                    ps_pauseup.setProgress(Float.parseFloat(String.valueOf(msg.obj)));
                    text_pauseup.setText(String.valueOf(msg.obj));
                    break;
                case 3:
                    ps_pausedown.setProgress(Float.parseFloat(String.valueOf(msg.obj)));
                    text_pausedown.setText(String.valueOf(msg.obj));
                    break;
                case 4:
                    refersh.finishRefresh();
                    QMUITipDialog tipDialog;
                    tipDialog = new QMUITipDialog.Builder(getContext())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                            .setTipWord("刷新成功")
                            .create();
                    tipDialog.show();
                    refersh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tipDialog.dismiss();
                        }
                    }, 1500);
                    break;
                case 5:
                    refersh.finishRefresh();
                    tipDialog = new QMUITipDialog.Builder(getContext())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                            .setTipWord("请求数据失败")
                            .create();
                    tipDialog.show();
                    refersh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tipDialog.dismiss();
                        }
                    }, 1500);
                    break;
                case 6:
                    // 这里是修改最后的更新时间
                    last_update.setText("最后更新时间:"+msg.obj);
                default:
                    break;
            }
        }
    };


    private void init(View view) {
        // 对话框存储rtsp流地址
        // 判断是否存储了rtsp数据
        if("".equals(GetData(view.getContext(), "rtsp"))){
            final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
            builder.setTitle("rtsp流地址")
                    .setPlaceholder("请输入rtsp流地址")
                    .setDefaultText("rtsp://192.168.123.62:8554/unicast")
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            CharSequence text = builder.getEditText().getText();
                            if (text != null && text.length() > 0) {
                                // 存储数据
                                SetData(view.getContext(),"rtsp", text.toString());
                                // 手动更新
                                url=text.toString();
                                // 播放器播放视频
                                videoPlayer.setUp(url, false, "");
                                videoPlayer.startPlayLogic();
                                // 设置地址
                                r_adder.setText(url);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), "请填入rtsp流地址", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        }else{
            // 更新rtsp流地址
            url=GetData(view.getContext(),"rtsp");
        }
        // 显示rtsp地址
        r_adder.setText(url);
        videoPlayer = view.findViewById(R.id.video_player);
        //默认比例
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        //开启硬解以及渲染优化
        GSYVideoType.enableMediaCodec();
        //播放的时候不缓存
        videoPlayer.setUp(url, false, "");
        videoPlayer.startPlayLogic();
        // 先自动同步数据
        getDatasync();
        //刷新控件
        refersh=view.findViewById(R.id.pull_to_refresh);
        refersh.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {
            }
            @Override
            public void onMoveRefreshView(int offset) {
            }
            @Override
            public void onRefresh() {
                getDatasync();
            }
        });
    }

    // 返回的data数据
    public static class ReturnData{
        public int code;
        public String msg;
        public Data data;
    }

    // json解析数据
    public static class Data{
        public int heart;
        public int h_pressure;
        public int l_pressure;
        public String update;
    }

    public void getDatasync(){
        // 执行http请求
        Log.e("xiaoyou","执行http请求");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(api).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("xiaoyou","执行请求失败");
                Log.e("xiaoyou",e.toString());
                Message msg = handler.obtainMessage();
                msg.what = 5;
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
                        // 判断数据是否获取成功
                        //修改数据  显示心率，收缩压，舒张压，最后更新时间
                        new Thread(new RThread(),res.data.heart+",1").start();
                        new Thread(new RThread(),res.data.h_pressure+",2").start();
                        new Thread(new RThread(),res.data.l_pressure+",3").start();
                        // 显示数据的最后更新时间
                        Message msg2 = handler.obtainMessage();
                        msg2.what = 6;
                        msg2.obj = res.data.update;
                        handler.sendMessage(msg2);
                        //线程通信通知
                        msg.what = 4;
                    } catch (Exception e){
                        // what为5 说明更新数据失败
                        msg.what = 5;
                    }
                } else {
                    msg.what = 5;
                }
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
    }


//    @Override
//    public void onBackPressed() {
////        //先返回正常状态
////        if(videoPlayer.isIfCurrentIsFullscreen()){
////            videoPlayer.backfull();
////        }else{
////            videoPlayer.setVideoAllCallBack(null);
////            super.onBackPressed();
////        }
//    }

}

