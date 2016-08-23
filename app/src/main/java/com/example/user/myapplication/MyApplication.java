package com.example.user.myapplication;

import android.app.Application;

import com.example.user.myapplication.model.PokemonInfo;
import com.example.user.myapplication.model.PokemonType;
import com.example.user.myapplication.model.SearchPokemonInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by User on 2016/8/18.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //與parse 註冊 subclass
        ParseObject.registerSubclass(PokemonInfo.class);
        ParseObject.registerSubclass(PokemonType.class);
        ParseObject.registerSubclass(SearchPokemonInfo.class);

        //初始化 Parse 資料庫
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .enableLocalDataStore()
                .applicationId("d41d8cd98f00b204e9800998ecf8427e")
                .server("http://140.112.30.43:1337/parse")
                .build());

        //初始化 ImageLoader

        //1. Set defaultOptions
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        //2.初始化
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheSize(50*1024*1024)
                .diskCacheFileCount(100)
                .build();

        //3.實體化
        ImageLoader.getInstance().init(config);
    }
}
