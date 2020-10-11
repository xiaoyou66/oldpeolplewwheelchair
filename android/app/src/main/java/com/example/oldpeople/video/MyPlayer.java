package com.example.oldpeople.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oldpeople.R;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class MyPlayer extends StandardGSYVideoPlayer {

    TextView time;//显示时间地控件
    TextView netspeed;//网络速度的控件
    com.ldoublem.loadingviewlib.LVCircular loading;//加载图标
    FrameLayout player;//播放器对象
    LinearLayout control;//空气器
    TextView scale;//比例控制按钮
    TextView rotal;//旋转按钮
    TextView jingxiang;//镜像按钮
    TextView full;//全屏按钮

    //控件初始化事件
    @Override
    protected void init(Context context) {
        super.init(context);
        //获取控件
        time=findViewById(R.id.now_time);
        netspeed=findViewById(R.id.net_speed);
        loading=findViewById(R.id.lv_Circular);
        //控件设置阳光效果
        loading.setViewColor(Color.rgb(255, 100, 100));
        loading.setRoundColor(Color.rgb(255, 100, 100));
        loading.startAnim();
        //控制器视图
        control=findViewById(R.id.control);
        //赋值播放器
        player=findViewById(R.id.surface_container);
        player.setOnClickListener((View v)->{
            if(control.getVisibility()==VISIBLE) {
                control.setVisibility(GONE);
            } else {
                control.setVisibility(VISIBLE);
            }
        });
        //比例的切换
        scale=findViewById(R.id.scale);
        scale.setOnClickListener((View v)->{
            switch (scale.getText().toString()){
                case "默认比例":
                    GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
                    scale.setText("16:9");
                    break;
                case "16:9":
                    GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
                    scale.setText("全屏裁剪显示");
                    break;
                case "全屏裁剪显示":
                    GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
                    scale.setText("全屏拉伸显示");
                    break;
                case "全屏拉伸显示":
                    GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
                    scale.setText("4:3");
                    break;
                case "4:3":
                    GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
                    scale.setText("默认比例");
                    break;
                default:
                    break;
            }
            //重新渲染
            startPrepare();
        });
        //旋转功能
        rotal=findViewById(R.id.rota);
        rotal.setOnClickListener((View view)->{
            //旋转90度
            player.setRotation(player.getRotation()+90);
        });
        //镜像功能
        jingxiang=findViewById(R.id.jinxiang);
        jingxiang.setOnClickListener((View view)->{
            switch (jingxiang.getText().toString()){
                case "正常镜像":
                    player.setScaleX(-1);
                    jingxiang.setText("左右镜像");
                    break;
                case "左右镜像":
                    player.setScaleY(-1);
                    jingxiang.setText("上下镜像");
                    break;
                case "上下镜像":
                    player.setScaleY(-1);
                    player.setScaleX(-1);
                    jingxiang.setText("翻转镜像");
                    break;
                case "翻转镜像":
                    player.setScaleY(1);
                    player.setScaleX(1);
                    jingxiang.setText("正常镜像");
                    break;
                default:
                    break;
            }
        });
        //全屏
        full=findViewById(R.id.full);
        if(isIfCurrentIsFullscreen()){
            full.setText("退出全屏");
        }else{
            full.setText("全屏播放");
        }
        full.setOnClickListener((View view)->{
            if("退出全屏".equals(full.getText().toString())){
                backFromFull(context);
                full.setText("全屏播放");
            }else{
                startWindowFullscreen(context,true,true);
                full.setText("退出全屏");
            }
        });
    }


    public void backfull(){
        backFromFull(getContext());
    }


    @Override
    public void onBufferingUpdate(final int percent) {
        //这里就简单的负责时间的刷新
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //设置时间
        time.setVisibility(VISIBLE);
        netspeed.setVisibility(VISIBLE);
        Date date = new Date(System.currentTimeMillis());
        time.setText(simpleDateFormat.format(date));
        //设置当前信号状态
        netspeed.setText(super.mNetSate);
        //控件自动消失
        loading.stopAnim();
        loading.setVisibility(GONE);
        //获取是否全屏
        if(isIfCurrentIsFullscreen()){
            full.setText("退出全屏");
        }else{
            full.setText("全屏播放");
        }
        //自动判断当前视频状态
        if(mCurrentState==2){
            loading.stopAnim();
            loading.setVisibility(GONE);
        }else{
            loading.setVisibility(VISIBLE);
            loading.startAnim();
        }
    }

    @Override
    public void onPrepared() {
        //加载成功的回调事件
        super.onPrepared();
        //控件自动消失
        loading.stopAnim();
        loading.setVisibility(GONE);
    }


    public MyPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyPlayer(Context context) {
        super(context);
    }

    public MyPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;
        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;
        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }
}