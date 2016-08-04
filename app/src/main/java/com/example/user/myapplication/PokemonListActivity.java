package com.example.user.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.myapplication.adapter.PokemonListViewAdapter;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;

import java.util.ArrayList;

/**
 * Created by user on 2016/7/25.
 */
public class PokemonListActivity extends CustomizedActivity implements AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
    //宣告變數
    PokemonListViewAdapter adapter;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list); //Setting Layout

        //找listview 物件,設定變數  (類型) findViewById (Layout.id)
        ListView listview = (ListView) findViewById(R.id.listView);

    ////////////////////////////////////////////////////////////
        //讀取資料
        //1.把資料丟到工具(model)中
        OwningPokemonDataManager dataManager = new OwningPokemonDataManager(this);


        //2.使用者所選擇的神奇寶貝
        int selectedOptionIndex = getIntent().getIntExtra(MainActivity.optionSelectedKey,0);

        //3.接收處理過的資料(大量神奇寶貝資訊與起始的三隻神奇寶貝資料)
        //3-1.接收起始的預設大量神奇寶貝資料
        ArrayList<PokemonInfo> pokemonInfos = dataManager.getPokemonInfos();
        //3-2.先接收三隻起始的神奇寶貝資料,再根據使用者的選擇加入指定的神奇寶貝資料
        PokemonInfo[] initThreePokemons = dataManager.getInitThreePokemonInfos();
        pokemonInfos.add(0,initThreePokemons[selectedOptionIndex]);

    /////////////////////////////////////////////////////////
        //設定Adapter
        adapter = new PokemonListViewAdapter(
                this, //context
                R.layout.row_view_pokemon_list, //row view layout id(file_name)
                pokemonInfos); //data
        //將Adapter設定到listview上
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    ////////////////////////////////////////////////////////////
        //AlertDialog
        //新增一個alertdialog 使用builder的方式建造 生成AlertDialog<靜態函數>
        //訊息//標題//取消此次行為<title,listener>//執行此次行為<title,listener>//對話框可否強制消失<backbutton>//生成
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("你確定要丟棄選取的神奇寶貝們嗎?")
                .setTitle("警告")
                .setNegativeButton("取消", this)
                .setPositiveButton("確定", this)
                .setCancelable(false)
                .create();
    }

    ///////////////////////////////////////////////////////////////////////
    //Setting Action bar 增加AlertDialog 使原function多一層確認
    ////1.在Activity加入action bar (導入xml)
    ////2.Action bar 選取後要先跳出AlertDialog
    ////3.重新撰寫取消及確定執行的function
    ////4.取消>>回覆前介面 // 確定 >> 執行該功能
    //加入action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_action_bar_menu, menu);
        return true; //表示一定會顯示action bar的畫面
    }

    //2.設定Action bar 選取後的功能
    //設定點選不同icon的邏輯
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete) {
            Log.d("menuItem", "action_delete");
            alertDialog.show();
            return true;
        } else if (itemId == R.id.action_heal) {
            Log.d("menuItem", "action_heal");
            return true;
        } else if (itemId == R.id.action_setting) {
            Log.d("menuItem", "action_setting");
            return true;
        }
        return false;
    }


    public final static int detailActivityRequestCode = 1;
    public final static int listRemove = 1;
    public final static int listLevelup = 2;

    //把pokemoninfo拿出來後產生intent
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PokemonInfo pokemonInfo = adapter.getItem(position);
        Intent intent = new Intent(PokemonListActivity.this, PokemonDetailActivity.class);
        intent.putExtra(PokemonInfo.parcelKey, pokemonInfo);
        startActivityForResult(intent, detailActivityRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == listRemove) {
            //1.接收要刪除的pokemon name 與 key
            String nameToRemove = data.getStringExtra(PokemonInfo.nameKey);

            //2.從name 去得到此pokemon的完整資訊
            //利用Adapter 已寫好的 getItemWitnName function完成本工作
            PokemonInfo pokemonInfo = adapter.getItemWithName(nameToRemove);

            //3.執行刪除動作並通知使用者
            if (pokemonInfo != null) {
                adapter.remove(pokemonInfo);
                Toast.makeText(this, pokemonInfo.name + "已存入電腦中", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == listLevelup) {
            //1.接收要刪除的pokemon name 與 key
            String nameToRemove = data.getStringExtra(PokemonInfo.nameKey);

            //2.從name 去得到此pokemon的完整資訊
            //利用Adapter 已寫好的 getItemWitnName function完成本工作
            PokemonInfo pokemonInfo = adapter.getItemWithName(nameToRemove);

            //3.執行LevelUp並通知使用者
            if (pokemonInfo != null) {
                //被選到的pokemon level +1
                int level = Integer.valueOf(pokemonInfo.level);
                level += 1;
                pokemonInfo.level = level;
                //更新pokemon資訊
                adapter.update(pokemonInfo);
                Toast.makeText(this, pokemonInfo.name + "已升級", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_NEGATIVE) {
            Toast.makeText(this, "取消丟棄", Toast.LENGTH_SHORT).show();
        } else if (which == AlertDialog.BUTTON_POSITIVE) {
            //把選取的pokemon info移除
            for (PokemonInfo pokemonInfo : adapter.selectedPokemon) {
                adapter.remove(pokemonInfo);
            }
            //清除選取的pokemon
            adapter.selectedPokemon.clear();
            Toast.makeText(this, "丟棄完畢", Toast.LENGTH_SHORT).show();

        }
    }
}