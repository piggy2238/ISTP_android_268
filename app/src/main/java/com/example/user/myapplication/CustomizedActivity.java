package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by User on 2016/8/2.
 */
public class CustomizedActivity extends AppCompatActivity {
//讓所有的Activity 在被按下HOME都能背景執行,而不是destroy > 不讓Root Activity被移除
    @Override
    public void onBackPressed() {
        //先判斷是否為root activity
        if(isTaskRoot()){
            //如為root activity, 按HOME後背景執行
            moveTaskToBack(true);
        }else{
            //一般HOME 的行為模式
            super.onBackPressed();
        }

    }
}
