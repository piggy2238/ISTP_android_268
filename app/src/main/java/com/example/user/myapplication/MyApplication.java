package com.example.user.myapplication;

import android.app.Application;

import com.example.user.myapplication.model.PokemonInfo;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by User on 2016/8/11.
 *
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //跟 Parse 註冊 subclass
        ParseObject.registerSubclass(PokemonInfo.class);
        //初始化 Parse 資料庫
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .enableLocalDataStore()
                .applicationId("n009dgPFQSptN0d1IvevazNuo4C7joktGHrrlebY")
                .clientKey("4iYDGoylC6K2UBxstZoaNvaVexUwcLKGEU60iaPI")
                .server("https://parseapi.back4app.com/")
                .build());
    }
}
