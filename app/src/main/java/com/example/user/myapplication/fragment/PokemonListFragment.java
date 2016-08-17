package com.example.user.myapplication.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.myapplication.MainActivity;
import com.example.user.myapplication.PokemonDetailActivity;
import com.example.user.myapplication.R;
import com.example.user.myapplication.adapter.PokemonListViewAdapter;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/8/5.
 * 將原本製作的PokemonListActivity轉換成Fragment
 */
public class PokemonListFragment extends Fragment implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, FindCallback<PokemonInfo>{

    //宣告變數
    private Activity activity;
    private ArrayList<PokemonInfo> pokemonInfos;
    private PokemonListViewAdapter adapter;
    private AlertDialog alertDialog;

    public final static int detailActivityRequestCode = 1;
    public final static int listRemove = 1;
    public final static int listLevelup = 2;

    //建立一個fragment樣板
    public static PokemonListFragment newInstance(){
        PokemonListFragment fragment = new PokemonListFragment();
        return fragment;

    }

    //判斷資料是否存在於資料庫, 預設值為false
    public static final String recordInDBKey = "recordInDBKey";
/*
* 生成物件及讀取資料
* */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        prepareListViewData();

    }

/*
* 與UI操作有關 Adapter Setting
* */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Find View (寫法與Activity不同)
        View fragmentView = inflater.inflate(R.layout.activity_pokemon_list,container,false);
        ListView listView = (ListView)fragmentView.findViewById(R.id.listView);

        //Setting Adapter
        adapter = new PokemonListViewAdapter(
                activity, //context
                R.layout.row_view_pokemon_list, //row view layout id(file_name)
                pokemonInfos); //data
        //將Adapter設定到listview上
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //Setting Dialog
        alertDialog = new AlertDialog.Builder(activity)
                .setMessage("你確定要丟棄選取的神奇寶貝嗎?")
                .setTitle("警告")
                .setNegativeButton("取消", this)
                .setPositiveButton("確定", this)
                .setCancelable(false)
                .create();

        //顯示OptionMenu
        setHasOptionsMenu(true);

        return fragmentView;
    }

    //////////////////////////////////////////////////////////////////////////
    //Dialog 功能開發
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_NEGATIVE) {
            Toast.makeText(activity, "取消丟棄", Toast.LENGTH_SHORT).show();
        } else if (which == AlertDialog.BUTTON_POSITIVE) {
            //soundPool.play(pokemon_delete,1,1,0,0,0);
            //把選取的pokemon info移除
            for (PokemonInfo pokemonInfo : adapter.selectedPokemon) {
                removePokemonInfo(pokemonInfo);
            }
            //清除選取的pokemon
            adapter.selectedPokemon.clear();
            Toast.makeText(activity, "丟棄完畢", Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //跳轉頁功能開發
    //把pokemoninfo拿出來後產生intent
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PokemonInfo pokemonInfo = adapter.getItem(position);
        Intent intent = new Intent(activity, PokemonDetailActivity.class);
        intent.putExtra(PokemonInfo.parcelKey, pokemonInfo);
        startActivityForResult(intent, detailActivityRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == listRemove) {

            String nameToRemove = data.getStringExtra(PokemonInfo.nameKey);
            PokemonInfo pokemonInfo = adapter.getItemWithName(nameToRemove);

             if (pokemonInfo != null) {
                 removePokemonInfo(pokemonInfo);
                 adapter.selectedPokemon.remove(pokemonInfo);
                 Toast.makeText(activity, pokemonInfo.getName() + "已存入電腦中", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == listLevelup) {

            String nameToRemove = data.getStringExtra(PokemonInfo.nameKey);
            PokemonInfo pokemonInfo = adapter.getItemWithName(nameToRemove);

            if (pokemonInfo != null) {
                int level = Integer.valueOf(pokemonInfo.getLevel());
                level += 1;
                pokemonInfo.setLevel(level);
                adapter.update(pokemonInfo);
                Toast.makeText(activity, pokemonInfo.getName() + "已升級", Toast.LENGTH_LONG).show();
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////
    //Action Bar Change to Drawer
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_action_bar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete) {
            Log.d("menuItem", "action_delete");
            if(adapter.selectedPokemon.size()>0) {
                alertDialog.show();
            }
            return true;
        } else if (itemId == R.id.action_heal) {
            Log.d("menuItem", "action_heal");
            //soundPool.play(healing_sound,1,1,0,0,0);
            //1.抓取被選取的神奇寶貝
            //2.更新hp資料 = max hp
            for (PokemonInfo pokemonInfo : adapter.selectedPokemon) {
                if(pokemonInfo !=null){
                    if(pokemonInfo.getCurrentHP() != pokemonInfo.getMaxHP()) {
                        pokemonInfo.setCurrentHP(Integer.valueOf(pokemonInfo.getMaxHP()));
                        adapter.update(pokemonInfo);
                        Toast.makeText(activity, pokemonInfo.getName() + "已補血", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity, pokemonInfo.getName() + "滿血中", Toast.LENGTH_SHORT).show();
                    }
                    pokemonInfo.isSelected = false;
                }
            }
            //清除選取的pokemon
            adapter.selectedPokemon.clear();

            return true;
        } else if (itemId == R.id.action_setting) {
            Log.d("menuItem", "action_setting");
            return true;
        }
        return false;
    }


    //////////////////////////////////////////////////////////////////////////
    //1.讀取 ListView 資料 2.執行與 DB 的資料同步
    /*設計重點:從 SharePreference 讀取資料 並判斷是否存在於資料庫
    * 初次使用:載入 local 資料 並於資料表同步
    * 舊的使用者:利用getQuery 從 兩邊 DB 拿取資料
    **/
    public void prepareListViewData(){

        pokemonInfos = new ArrayList<>();

        SharedPreferences preferences = activity.getSharedPreferences(Application.class.getName(), Activity.MODE_PRIVATE);
        boolean recordInDB = preferences.getBoolean(recordInDBKey, false);

        OwningPokemonDataManager dataManager = new OwningPokemonDataManager(activity); //CSV 資料、讀取資料所需的函式先存於物件中

        if(!recordInDB){
            //初次使用 save to DB
            dataManager.loadListViewData(); //載入CSV資料-訓練師所選擇的神奇寶貝與基本的神奇寶貝資料群

            /**目的:放入系統一開始給予訓練師的基本 神奇寶貝群
             *1. 從 dataManager 把資料取回 暫存於 ArrayList<PokeomnInfo> 中
             *2. 利用迴圈方式將取回的資料正式的寫入 pokemoInfos
             */
            ArrayList<PokemonInfo> temInfos = dataManager.getPokemonInfos();
            for (PokemonInfo pokemonInfo : temInfos){
                pokemonInfos.add(pokemonInfo);
            }

            /**目的:放入訓練師一開始選的神奇寶貝夥伴 (小火龍,傑尼龜,妙蛙種子)的資訊
             * 方法:
             * 1. 從 dataManager 把三隻的完整資訊都取回 存於 PokemonInfo[]
             * 2. 利用 getIntent 得到使用者選擇的編號(selectedPokemonIndex)
             * 3. 加入pokemonInfos 中
             * */
            PokemonInfo[] initThreePokemons = dataManager.getInitThreePokemonInfos();
            int selectedPokemonIndex = activity.getIntent().getIntExtra(MainActivity.optionSelectedKey,0);
            pokemonInfos.add(0,initThreePokemons[selectedPokemonIndex]);

            //執行資料表初始化,將資料放入 DB 中, 目前狀況:recordInDB --> true, commit起來
            PokemonInfo.initTable(pokemonInfos);
            preferences.edit().putBoolean(recordInDBKey,true).commit();

        }else{
            //舊的使用者 Load from DB
            ParseQuery<PokemonInfo> query = PokemonInfo.getQuery();
            /**先從local DB 取資料*/
            query.fromPin(PokemonInfo.localDBTableName).findInBackground(this);
            /** 從remote DB 取資料*/
            query = PokemonInfo.getQuery();
            query.findInBackground(this);
        }


    }


    //////////////////////////////////////////////////////////////////////////
    //目的:更新 ListView 顯示的內容
    /*1. 將舊有資料清空,再填入修改過的資料
    * 2. 通知 adapter data 有更動, 須同步調整 ListView 的顯示內容
    */
    @Override
    public void done(List<PokemonInfo> objects, ParseException e) {
        if (e == null){
            pokemonInfos.clear();
            for (PokemonInfo object : objects){
                pokemonInfos.add(object);
            }
       }
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //更新 remove 功能
    /*目的: 當使用者刪除 local DB pokemon 的時候 能夠 同時刪除 remote DB 的 pokemon
     */

    public void removePokemonInfo(PokemonInfo pokemonInfo){
        //Remove from local
        adapter.remove(pokemonInfo);
        //Remove from DB
        pokemonInfo.unpinInBackground(PokemonInfo.localDBTableName);
        pokemonInfo.deleteEventually();
    }
}
