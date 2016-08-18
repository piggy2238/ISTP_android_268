package com.example.user.myapplication.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by User on 2016/8/18.
 * Parse 三部曲
 * 1. 建立對應的程式物件 (Parse Subclass)
 * 2. 註冊物件 (MyApplication)
 * 3. 呼叫方法 (SearchListFragment)
 *
 */
@ParseClassName("PokemonType")
public class PokemonType extends ParseObject {

    //要取的資料要給予一個key
    public final static String typeArray = "all";//Query 遠端資料 Key value = all

    //從 remote 取回所有 ParseObject List
    public static ParseQuery<PokemonType> getQuery(){
        return ParseQuery.getQuery(PokemonType.class);
    }

    //上傳資料時得到型別
    public ArrayList<String> getTypeArray(){
        return (ArrayList<String>) get(typeArray);
    }



}
