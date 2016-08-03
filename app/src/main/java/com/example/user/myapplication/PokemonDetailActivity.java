package com.example.user.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.myapplication.model.PokemonInfo;
import com.squareup.picasso.Picasso;

/**
 * Created by user on 2016/7/28.
 */
public class PokemonDetailActivity extends CustomizedActivity {

    PokemonInfo mpokemonInfo;
    Resources mRes;
    String packageName;


    //UI 成員變數
    ImageView img;
    TextView nameText;
    TextView levelText;
    TextView currentHp;
    TextView maxHp;
    TextView type_1;
    TextView type_2;
    TextView[] skillText;
    ProgressBar hbBar;
    Picasso mPicasso;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ////初始化
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detail_view);
            //需by case的create
            //讀取res/下的檔案 能轉換為java 可讀型式
            mRes = getResources();
            //加入圖片
            mPicasso = Picasso.with(this);
            packageName = getPackageName();
            //接收從ListActivity 傳送過來的資料
            mpokemonInfo = getIntent().getParcelableExtra(PokemonInfo.parcelKey);
            skillText = new TextView[PokemonInfo.numCurrentSkills];

        //一般create
            setView();
    }


    private void setView(){
        //建立DetailActivity 與 Layout 間的鍵結
        img = (ImageView)findViewById(R.id.detail_appearance_img);
        nameText    = (TextView)findViewById(R.id.name_text);
        levelText   = (TextView)findViewById(R.id.level_text);
        currentHp   = (TextView)findViewById(R.id.currentHP_text);
        maxHp       = (TextView)findViewById(R.id.maxHP_text);
        type_1      = (TextView)findViewById(R.id.type_1_text);
        type_2      = (TextView)findViewById(R.id.type_2_text);
        hbBar       = (ProgressBar)findViewById(R.id.HP_progressBar);
            //加入技能
        for (int i = 0; i< PokemonInfo.numCurrentSkills; i++){
            int skillTextId = mRes.getIdentifier(String.format("skill_%d_text",i+1),"id",packageName);
            skillText[i] = (TextView)findViewById(skillTextId);
        }

        //bind with data
        mPicasso.load(mpokemonInfo.detailImgId).into(img);
        nameText.setText(mpokemonInfo.name);
        levelText.setText(String.valueOf(mpokemonInfo.level));
        currentHp.setText(String.valueOf(mpokemonInfo.currentHP));
        maxHp.setText(String.valueOf(mpokemonInfo.maxHP));
        type_1.setText(String.valueOf(mpokemonInfo.type_1));
        type_2.setText(String.valueOf(mpokemonInfo.type_2));

        if(mpokemonInfo.type_1 != -1) {
            type_1.setText(PokemonInfo.typeNames[mpokemonInfo.type_1]);
        }
        else {
            type_1.setText("");
        }

        if(mpokemonInfo.type_2 != -1) {
            type_2.setText(PokemonInfo.typeNames[mpokemonInfo.type_2]);
        }
        else {
            type_2.setText("");
        }




        for (int i = 0 ; i<PokemonInfo.numCurrentSkills; i++){
            if (mpokemonInfo.skill[i]!=null){
                skillText[i].setText(mpokemonInfo.skill[i]);
            }else{
                skillText[i].setText("");
            }
        }





        int progress = (int)((((float)(mpokemonInfo.currentHP))/mpokemonInfo.maxHP)*100);
        hbBar.setProgress(progress);
    }





}



