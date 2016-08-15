package com.example.user.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
    public final static String parcelKey = "PokemonInfo.parcel";
    public final static String nameKey = "PokemonInfo.name";
    //新增 Parse 存取資料會用到的 Key = 雲端資料表的欄位名稱
    public final static String listImgKey = "listImgId";
    public final static String levelKey = "level";
    public final static String currentHPKey = "currentHP";
    public final static String maxHPKey = "maxHP";
    public final static String type1Key = "type1";
    public final static String type2Key = "type2";
    public final static String skillKey = "skill";
    public final static String detailImgIdKey = "detailImgId";
    //自動把 public 的屬性封裝成getter(讀取) 和setter (設定)-存到Parse 的內部儲存空間
    public final static int numCurrentSkills = 4;
    public static String[] typeNames ;

    //因為要傳遞值 使用plugin 進行批次傳遞 存於parcle 作為初始化數值 再傳遞到下個Activity
    //但因為detail 還沒做, 所以這邊先不進行
//    public int detailImgId;
//    public int listImgId;
//    public String name;
//    public int level;
//    public int currentHP;
//    public int maxHP;
//    public int type_1;
//    public int type_2;
    public String[] skill = new String[numCurrentSkills];

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
        dest.writeInt(this.getType_1());
        dest.writeInt(this.getType_2());
        dest.writeInt(this.getDetailImgId());
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

    // 按右鍵 > generate > getter and setter
    public int getlistImgId() {
        return getInt(listImgKey);
    }

    public void setlistImgId(int listImgId) {
        put(listImgKey, listImgId);
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
        for(String skillName : skill){
            skillList.add(skillName);
        }
        put(skillKey,skill);
        this.skill = skill;
    }

    public int getDetailImgId() {
        return getInt(detailImgIdKey);
    }

    public void setDetailImgId(int detailImgId) {
        put(detailImgIdKey,detailImgId);
    }
}
