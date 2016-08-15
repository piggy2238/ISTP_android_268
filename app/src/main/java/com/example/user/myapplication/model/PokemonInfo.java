package com.example.user.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
    //自動把 public 的屬性封裝成getter(讀取) 和setter (設定)
    public final static int numCurrentSkills = 4;
    public static String[] typeNames ;

    //因為要傳遞值 使用plugin 進行批次傳遞 存於parcle 作為初始化數值 再傳遞到下個Activity
    //但因為detail 還沒做, 所以這邊先不進行
    public int detailImgId;
    public int imgId;
    public String name;
    public int level;
    public int currentHP;
    public int maxHP;
    public int type_1;
    public int type_2;
    public String[] skill = new String[numCurrentSkills];

    public boolean isSelected = false;
    public boolean isHealing = false;




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.level);
        dest.writeInt(this.currentHP);
        dest.writeInt(this.maxHP);
        dest.writeInt(this.detailImgId);
        dest.writeInt(this.type_1);
        dest.writeInt(this.type_2);
        dest.writeStringArray(this.skill);
    }

    public PokemonInfo() {
    }

    protected PokemonInfo(Parcel in) {
        this.name = in.readString();
        this.level = in.readInt();
        this.currentHP = in.readInt();
        this.maxHP = in.readInt();
        this.detailImgId = in.readInt();
        this.type_1 = in.readInt();
        this.type_2 = in.readInt();
        this.skill = in.createStringArray();
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
}
