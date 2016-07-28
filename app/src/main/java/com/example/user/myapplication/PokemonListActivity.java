package com.example.user.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.myapplication.adapter.PokemonListViewAdapter;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;

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


        //複雜板listView:1.建立pokemon_data與Pokemon的資料結構 2.建立Layout file
        //接收工具(model)使用的結果
        ArrayList<PokemonInfo> pokemonInfos = dataManager.getPokemonInfos();

        //設定Adapter
        PokemonListViewAdapter adapter = new PokemonListViewAdapter(
                this, R.layout.row_view_pokemon_list, pokemonInfos
        );


        //將Adapter設定到listview上
        listview.setAdapter(adapter);



    }

    //加入action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_action_bar_menu,menu);
        return true; //表示一定會顯示action bar的畫面
    }
    //設定點選不同icon的邏輯
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int intemId = item.getItemId();
        if (intemId == R.id.action_delete){
            Log.d("menuItem", "action_delete");
            return true;
        }else if(intemId == R.id.action_heal){
            Log.d("menuItem", "action_heal");
            return true;
        }else{
            Log.d("menuItem", "action_setting");
            return true;
        }

        return false;
    }
}
