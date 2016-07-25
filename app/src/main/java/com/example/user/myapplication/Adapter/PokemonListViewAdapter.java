package com.example.user.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.myapplication.model.PokemonInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.user.myapplication.R;

/**
 * Created by user on 2016/7/25.
 */
public class PokemonListViewAdapter extends ArrayAdapter<PokemonInfo> {

    //加個m 遍認為成員 (member) 物件
    int mRowLayoutId;
    //根據id產生列的view
    LayoutInflater minflater;
    //放圖
    Picasso mPicasso;


    //----------------------初始化 start
    //選擇繼承類別
    //                                             |---LayoutId---|
    public PokemonListViewAdapter(Context context, int rowLayoutId, List<PokemonInfo> objects) {
        super(context, rowLayoutId, objects);

        mRowLayoutId = rowLayoutId;
        minflater = LayoutInflater.from(context); //系統提供的API 給他一個context他會回傳一個Layout的物件回來

    //----------------------初始化 end
    //----------------------
        //Test Picasso, 確定取用成功
        mPicasso = Picasso.with(context);
    }


    //getView = 拿到整個row的view,決定整個view長的樣子

//  |--預計要顯示資料中的第幾個--|
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Position 取出既定的對應資料
        PokemonInfo pokemonInfo = getItem(position);
        ViewHolder viewHolder;
        //判斷傳進來的第二個參數是否為空值(未被初始化)
        if (convertView==null){ //first time
            //根據某個Layout去產生View
            convertView = minflater.inflate(mRowLayoutId, parent, false);
            //結束後就產生了我們要的row view (convertView)

            viewHolder = new ViewHolder(convertView,mPicasso);
            convertView.setTag(viewHolder); //放進去記起來
        }else{
            //若已經初始化,可以直接取出
            viewHolder = (ViewHolder)convertView.getTag(); //拿出Tag
        }

        viewHolder.setView(pokemonInfo);

        return convertView;
    }









    //-----------------建立 innerclass
    //記住layout物件    |--靜態變數--|
    public static class ViewHolder {

        //|--成員變數--|
        private ImageView img;
        private TextView name;
        private TextView level;
        private TextView hp;
        private TextView max_hp;
        private ProgressBar hp_bar;
        private Picasso mPicasso;


        //Constructoer 建構子
        public ViewHolder(View row_view, Picasso picasso){
            //找出相對應UI
            img =   (ImageView)row_view.findViewById(R.id.img);
            name =  (TextView)row_view.findViewById(R.id.name);
            level = (TextView)row_view.findViewById(R.id.level);
            hp =    (TextView)row_view.findViewById(R.id.hp);
            max_hp =(TextView)row_view.findViewById(R.id.max_hp);
            hp_bar =(ProgressBar)row_view.findViewById(R.id.hp_bar);
            mPicasso = picasso;
        }

        //SetView 得到一個參數=pokemon的info 把資料取出放到UI
        public void setView(PokemonInfo pokemonInfo){
            //textView 透過 setview設計內容
            //成員變數.setText(pokemon.(Info內的變數名稱)) 須注意資料型態上的轉換
            hp.setText(String.valueOf(pokemonInfo.currentHP));
            max_hp.setText(String.valueOf(pokemonInfo.maxHP));
            level.setText(String.valueOf(pokemonInfo.level));
            name.setText(String.valueOf(pokemonInfo.name));

            //progress 靠自己呈現內容
            int progress = (int)((((float)(pokemonInfo.currentHP))/pokemonInfo.maxHP)*100);
            hp_bar.setProgress(progress);
            //img 透過 picasso 設計內容
            mPicasso.load(pokemonInfo.imgId).into(img);


        }


    }

}
