package com.example.user.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.myapplication.model.OwningPokemonDataManager;

import java.util.ArrayList;

/**
 * Created by User on 2016/7/28.
 */
public class PokemonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemonlistview); //Setting Layout

        //------抓資料-----//
            //---宣告一個變數 抓Layout ID
        ListView mlistview = (ListView) findViewById(R.id.listView);

            //---使用model 中預設的method抓資料
                //--- 先創一個 mdataManager 管理資料 的 class
                OwningPokemonDataManager mdataManager = new OwningPokemonDataManager(this);

                //--- 利用這個 class中的method 實際把pokemon 的名字抓出來
                ArrayList<String> mPokemonNames = mdataManager.getPokemonNames();

        //------設定Adapter-------//
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(
                this,                                   //將這個Adapter直接使用在本內容中?
                android.R.layout.simple_list_item_1,    //使用官方版的layout
                mPokemonNames                           //變數資料來源

        );

        //-----安裝Adapter Listener
        mlistview.setAdapter(madapter);
    }

}
