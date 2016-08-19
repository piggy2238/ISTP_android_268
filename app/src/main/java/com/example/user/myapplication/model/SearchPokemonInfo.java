package com.example.user.myapplication.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * Created by User on 2016/8/18.
 */
@ParseClassName("PokemonInfo")
public class SearchPokemonInfo extends ParseObject{

    //一個資訊一個key
    public final static String nameKey = "name";
    public final static String hpKey = "hp";
    public final static String typeKey = "type";
    public final static String resIdKey = "resId";

    public static ParseQuery<SearchPokemonInfo> getQuery(){
        return ParseQuery.getQuery(SearchPokemonInfo.class);
    }

    public String getName(){
        return getString(nameKey);
    }

    public int gethp(){
        return getInt(hpKey);
    }

    public ArrayList<Integer> getTypeIndices(){
        return (ArrayList)get(typeKey);
    }

    public String getPokedex(){
        return getString(resIdKey);
    }
}
