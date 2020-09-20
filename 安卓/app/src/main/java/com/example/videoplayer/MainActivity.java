package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.videoplayer.fragment.ChartFragment;
import com.example.videoplayer.fragment.HomeFragment;
import com.example.videoplayer.fragment.MapFragment;


public class MainActivity extends AppCompatActivity{
    HomeFragment homeFragment;
    ChartFragment chartFragment;
    MapFragment mapFragment;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //给控件赋值
        homeFragment=new HomeFragment();
        chartFragment=new ChartFragment();
        mapFragment=new MapFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.appfragment,homeFragment);
        fragmentTransaction.commit();

        //查找各个控件，然后自动替换
        RadioButton r_home=findViewById(R.id.tab_home);
        RadioButton r_chart=findViewById(R.id.tab_charts);
        RadioButton r_map=findViewById(R.id.tab_map);
        MClick click=new MClick();
        //各个控件点击切换
        r_home.setOnClickListener(click);
        r_chart.setOnClickListener(click);
        r_map.setOnClickListener(click);
    }

    private class MClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            fragmentTransaction=getSupportFragmentManager().beginTransaction();
            switch (v.getId()){
                case R.id.tab_home:
                    fragmentTransaction.replace(R.id.appfragment,homeFragment);
                    break;
                case R.id.tab_charts:
                    fragmentTransaction.replace(R.id.appfragment,chartFragment);
                    break;
                case R.id.tab_map:
                    fragmentTransaction.replace(R.id.appfragment,mapFragment);
                    break;
                default:
                    break;
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
//        //先返回正常状态
//        if(videoPlayer.isIfCurrentIsFullscreen()){
//            videoPlayer.backfull();
//        }else{
//            videoPlayer.setVideoAllCallBack(null);
            super.onBackPressed();
//        }
    }

}
