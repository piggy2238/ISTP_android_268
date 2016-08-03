package com.example.user.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lab430 on 16/7/16.
 */
public class PokemonInfo implements Parcelable {

    //建立一個key
    public final static String parcelKey = "PokemonInfo.parcel";
    public final static String nameKey = "PokemonInfo.Key";

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
    //public boolean isHealing = false;




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
