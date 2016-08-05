package com.example.user.myapplication.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.myapplication.MainActivity;
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
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
