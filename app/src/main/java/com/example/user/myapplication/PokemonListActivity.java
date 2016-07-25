package com.example.user.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.myapplication.model.OwningPokemonDataManager;

import java.util.ArrayList;

/**
 * Created by user on 2016/7/25.
 */
public class PokemonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list); //Setting Layout

        //找listview 物件,設定變數  (類型) findViewById (Layout.id)
        ListView listview= (ListView)findViewById(R.id.listView);

        //把資料丟到工具(model)中
        OwningPokemonDataManager dataManager = new OwningPokemonDataManager(this);

        //接收工具(model)使用的結果
        ArrayList<String> pokemonNames = dataManager.getPokemonNames();

        //設定Adapter
        //每一烈顯示最簡單的listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                //|---Content---|,|--- 官方-Id (android.R.layout.)---| ,|---資料---|
                     this,          android.R.layout.simple_list_item_1, pokemonNames
        ); //使用官方提供的Layout,未來可使用自己設計的Layout


        //將Adapter設定到listview上
        listview.setAdapter(adapter);

    }
}
