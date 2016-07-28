package com.example.user.myapplication.model;

/**
 * Created by lab430 on 16/7/16.
 */
public class PokemonInfo {

    //建立一個key
    public final static String parcelKey = "PokemonInfo.parcel";

    //因為要傳遞值 使用plugin 進行批次傳遞 存於parcle 作為初始化數值 再傳遞到下個Activity
    //但因為detail 還沒做, 所以這邊先不進行

    public int imgId;
    public String name;
    public int level;
    public int currentHP;
    public int maxHP;

    public boolean isSelected = false;
    public boolean isHealing = false;


}
