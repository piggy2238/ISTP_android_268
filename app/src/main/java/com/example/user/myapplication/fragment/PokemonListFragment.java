package com.example.user.myapplication.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.user.myapplication.MainActivity;
import com.example.user.myapplication.R;
import com.example.user.myapplication.adapter.PokemonListViewAdapter;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;

import java.util.ArrayList;

/**
 * Created by User on 2016/8/5.
 * 將原本製作的PokemonListActivity轉換成Fragment
 */
public class PokemonListFragment extends Fragment {

    //宣告變數
    private Activity activity;
    ArrayList<PokemonInfo> pokemonInfos;
    private PokemonListViewAdapter adapter;
    private AlertDialog alertDialog;

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

        OwningPokemonDataManager dataManager = new OwningPokemonDataManager(activity);
        int selectedOptionIndex = activity.getIntent().getIntExtra(MainActivity.optionSelectedKey,0);
        pokemonInfos = dataManager.getPokemonInfos();

        PokemonInfo[] initThreePokemons = dataManager.getInitThreePokemonInfos();
        pokemonInfos.add(0,initThreePokemons[selectedOptionIndex]);
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
        alertDialog = new AlertDialog.Builder(this)
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



}
