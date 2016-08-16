package com.example.user.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lab430 on 16/7/16.
 * 0815 新增 Key 以便符合 Parse App
 * extends ParseObject 繼承的是 MyApplication
 */
@ParseClassName("PokemonInfo")
public class PokemonInfo extends ParseObject implements Parcelable {

    //建立一個key
    public final static String parcelKey = "parcel";
    public final static String nameKey = "name";
    //新增 Parse 存取資料會用到的 Key = 雲端資料表的欄位名稱
    public final static String imgIdKey = "imgId";
    public final static String levelKey = "level";
    public final static String currentHPKey = "currentHP";
    public final static String maxHPKey = "maxHP";
    public final static String type1Key = "type1";
    public final static String type2Key = "type2";
    public final static String skillKey = "skill";
    public final static String detailImgIdKey = "detailImgId";
    //自動把 public 的屬性封裝成getter(讀取) 和setter (設定)
    //右鍵 > refactor > Encapsulate fields
    public final static int numCurrentSkills = 4;
    public static String[] typeNames ;
    //辨別要存取的 local database 的資料表
    public static final String localDBTableName = PokemonInfo.class.getName();



    //因為要傳遞值 使用plugin 進行批次傳遞 存於parcle 作為初始化數值 再傳遞到下個Activity
    //但因為detail 還沒做, 所以這邊先不進行
    private int detailImgId;
    private int imgId;
    private String name;
    private int level;
    private int currentHP;
    private int maxHP;
    private int type_1;
    private int type_2;
    private String[] skill = new String[numCurrentSkills];

    public boolean isSelected = false;
    public boolean isHealing = false;




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getName());
        dest.writeInt(this.getLevel());
        dest.writeInt(this.getCurrentHP());
        dest.writeInt(this.getMaxHP());
        dest.writeInt(this.getDetailImgId());
        dest.writeInt(this.getType_1());
        dest.writeInt(this.getType_2());
        dest.writeStringArray(this.getSkill());
    }

    public PokemonInfo() {
    }

    protected PokemonInfo(Parcel in) {
        this.setName(in.readString());
        this.setLevel(in.readInt());
        this.setCurrentHP(in.readInt());
        this.setMaxHP(in.readInt());
        this.setDetailImgId(in.readInt());
        this.setType_1(in.readInt());
        this.setType_2(in.readInt());
        this.setSkill(in.createStringArray());
    }

    public static final Parcelable.Creator<PokemonInfo> CREATOR = new Parcelable.Creator<PokemonInfo>() {
        @Override
        public PokemonInfo createFromParcel(Parcel source) {
            return new PokemonInfo(source);
        }

        @Override
        public PokemonInfo[] newArray(int size) {
            return new PokemonInfo[size];
        }
    };

    public int getDetailImgId() {
        return getInt(detailImgIdKey);
    }

    public void setDetailImgId(int detailImgId) {
        put(detailImgIdKey, detailImgId);
    }

    public int getImgId() {
        return getInt(imgIdKey);
    }

    public void setImgId(int imgId) {
        put(imgIdKey, imgId);
    }

    public String getName() {
        return getString(nameKey);
    }

    public void setName(String name) {
        put(nameKey,name);
    }

    public int getLevel() {
        return getInt(levelKey);
    }

    public void setLevel(int level) {
        put(levelKey,level);
    }

    public int getCurrentHP() {
        return getInt(currentHPKey);
    }

    public void setCurrentHP(int currentHP) {
        put(currentHPKey,currentHP);
    }

    public int getMaxHP() {
        return getInt(maxHPKey);
    }

    public void setMaxHP(int maxHP) {
        put(maxHPKey,maxHP);
    }

    public int getType_1() {
        return getInt(type1Key);
    }

    public void setType_1(int type_1) {
        put(type1Key,type_1);
    }

    public int getType_2() {
        return getInt(type2Key);
    }

    public void setType_2(int type_2) {
        put(type2Key,type_2);
    }

    public String[] getSkill() {
        return skill;
    }

    public void setSkill(String[] skill) {
        List<String> skillList = new ArrayList<>(skill.length);
        for(String skillName : skill ){
            skillList.add(skillName);
        }
        put(skillKey,skillList);
        this.skill = skill;
    }

    /**從Parse 拿Query的Objects*/
    public static ParseQuery<PokemonInfo> getQuery(){
        return ParseQuery.getQuery(PokemonInfo.class);
    }

    /**初始化資料表
     * 1. 先取得lacal端新資料
     * 2. 刪除全部舊資料
     * 3. 重新放入新資料,再同步?!
     *
     * 2.5建立與資料庫同步的function (syncToDB) 以便在步驟3使用
     * */
    public static void initTable(final ArrayList<PokemonInfo> pokemonInfos){
        //2-0. 找尋雲端及local端的舊資料
        PokemonInfo.getQuery().fromPin(localDBTableName).findInBackground(new FindCallback<PokemonInfo>() {
            @Override
            public void done(List<PokemonInfo> objects, ParseException e) {
                if(e == null){
                    //2-1. 刪除雲端資料
                    for(PokemonInfo object :objects){
                        object.deleteEventually();
                    }
                    //2-2. 刪除local端資料
                    PokemonInfo.unpinAllInBackground(objects);

                    //3.重新放入資料後與資料庫同步
                    syncToDB(pokemonInfos);
                }
            }
        });
    }

    public static void syncToDB(List<PokemonInfo> pokemonInfos){
        //Save to local
        PokemonInfo.pinAllInBackground(localDBTableName, pokemonInfos);
        //Save to remote
        for( PokemonInfo pokemonInfo: pokemonInfos){
            pokemonInfo.saveEventually();
        }
    }


}
