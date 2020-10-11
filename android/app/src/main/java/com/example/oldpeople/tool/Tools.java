package com.example.oldpeople.tool;

import android.content.Context;
import android.content.SharedPreferences;

public class Tools {

    static public void SetData(Context context,String key,String value){
        SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        editor.putString(key,value);
        //步骤4：提交
        editor.apply();
    }


    static public String GetData(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

}
