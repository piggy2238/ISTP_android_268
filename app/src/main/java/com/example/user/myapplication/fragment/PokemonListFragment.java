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

    public final static String recordInDBKey = "recordInDBKey";

    View fragmentView = null;

    //建立一個fragment樣板
    public static PokemonListFragment newInstance(){
        PokemonListFragment fragment = new PokemonListFragment();
        return fragment;

    }
/*
* 生成物件及讀取資料
* */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        prepareListViewData();


    }

    //從 資料 準備
    public void prepareListViewData(){

        pokemonInfos = new ArrayList<>();

        SharedPreferences preferences = activity.getSharedPreferences(Application.class.getName(),Activity.MODE_PRIVATE);
        boolean recordInDB = preferences.getBoolean(recordInDBKey, false);

        OwningPokemonDataManager dataManager = new OwningPokemonDataManager(activity);
        dataManager.loadPokemonTypes();

        //初次使用建立新的資料庫
        if(!recordInDB){

            dataManager.loadListViewData();
            int selectedOptionIndex = activity.getIntent().getIntExtra(MainActivity.optionSelectedKey,0);
            PokemonInfo[] initThreePokemons = dataManager.getInitThreePokemonInfos();

            ArrayList<PokemonInfo> tempInfos = dataManager.getPokemonInfos();

            for(PokemonInfo pokemonInfo:tempInfos){
                pokemonInfos.add(pokemonInfo);
            }

            pokemonInfos.add(0,initThreePokemons[selectedOptionIndex]);

            //Save to DB
            PokemonInfo.initTable(pokemonInfos);
            preferences.edit().putBoolean(recordInDBKey,true).commit();
        }else{

            ParseQuery<PokemonInfo> query = PokemonInfo.getQuery();
            query.fromPin(PokemonInfo.localDBTableName).findInBackground(this);//從 local 取資料
//            query = PokemonInfo.getQuery();
//            query.findInBackground(this);//Load from DB; this 透過 callback 去取資料

        }
    }


/*
* 與UI操作有關 Adapter Setting
* */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragmentView == null){
            //Find View (寫法與Activity不同)
            fragmentView = inflater.inflate(R.layout.activity_pokemon_list,container,false);
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
        }

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
        Toast.makeText(activity, pokemonInfo.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, PokemonDetailActivity.class);
        intent.putExtra(PokemonInfo.parcelKey, pokemonInfo);
        startActivityForResult(intent, detailActivityRequestCode);
    }

    //接收從 detail 回傳的參數 並改變 ListView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == listRemove) {

            String nameToRemove = data.getStringExtra(PokemonInfo.nameKey);
            PokemonInfo pokemonInfo = adapter.getItemWithName(nameToRemove);

             if (pokemonInfo != null) {
                 adapter.remove(pokemonInfo);
                 adapter.selectedPokemon.clear();
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
//            for (PokemonInfo pokemonInfo : adapter.selectedPokemon) {
//                if(pokemonInfo !=null){
//                    if(pokemonInfo.getCurrentHP() != pokemonInfo.getMaxHP()) {
//                        pokemonInfo.setCurrentHP(Integer.valueOf(pokemonInfo.getMaxHP()));
//                        adapter.update(pokemonInfo);
//                        Toast.makeText(activity, pokemonInfo.getName() + "已補血", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(activity, pokemonInfo.getName() + "滿血中", Toast.LENGTH_SHORT).show();
//                    }
//                    pokemonInfo.isSelected = false;
//                }
//            }
//            //清除選取的pokemon
//            adapter.selectedPokemon.clear();

            return true;
        } else if (itemId == R.id.action_setting) {
            Log.d("menuItem", "action_setting");
            return true;
        }
        return false;
    }


    //刪除資料後 要通知 Adapter ListView 改變 (callback), 就能及時調整 View
    public void removePokemonInfo(PokemonInfo pokemonInfo){
        adapter.remove(pokemonInfo);

        pokemonInfo.unpinInBackground(PokemonInfo.localDBTableName);
        pokemonInfo.deleteEventually();

    }

    @Override
    public void done(List<PokemonInfo> objects, ParseException e) {
        if( e == null ){
            pokemonInfos.clear();
            for (PokemonInfo object : objects){
                pokemonInfos.add(object);
            }
        }

        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
