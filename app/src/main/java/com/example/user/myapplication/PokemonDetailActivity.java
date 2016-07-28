package com.example.user.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.myapplication.model.PokemonInfo;

/**
 * Created by user on 2016/7/28.
 */
public class PokemonDetailActivity extends AppCompatActivity {

    PokemonInfo mpokemonInfo;
    Resources mRes;
    String packageName;


    //UI 成員變數
    TextView nameText;
    TextView levelText;
    TextView currentHp;
    TextView maxHp;
    TextView type_1;
    TextView type_2;
    TextView[] skillText;
    ProgressBar hbBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        mpokemonInfo = (PokemonInfo)getIntent().getParcelableArrayExtra(PokemonInfo.parcelKey);
        skillText = new TextView[PokemonInfo.numCurrentSkills];
    }


    private void setView(){
        nameText    = (TextView)findViewById(R.id.name_text);
        levelText   = (TextView)findViewById(R.id.level_text);
        currentHp   = (TextView)findViewById(R.id.currentHP_text);
        maxHp       = (TextView)findViewById(R.id.maxHP_text);
        type_1      = (TextView)findViewById(R.id.type_1_text);
        type_2      = (TextView)findViewById(R.id.type_2_text);
        hbBar       = (ProgressBar)findViewById(R.id.HP_progressBar);
        for (int i = 0; i< PokemonInfo.numCurrentSkills; i++){
            int skillTextId = mRes.getIdentifier(String.format("skill_%d_text",i+1),"id",packageName);
            skillText[i] = (TextView)findViewById(skillText);

        }

        nameText.setText(mpokemonInfo.name);
        levelText.setText(String.valueOf(mpokemonInfo.level));
        currentHp.setText(String.valueOf(mpokemonInfo.currentHP));
        maxHp.setText(String.valueOf(mpokemonInfo.maxHP));
        type_1.setText(String.valueOf(mpokemonInfo.type_1));
        type_2.setText(String.valueOf(mpokemonInfo.type_2));
        for (int i = 0 ; i<PokemonInfo.numCurrentSkills; i++){
            if (mpokemonInfo.skill[i]!=null){
                skillText[i].setText(mpokemonInfo.skill[i]);
            }else{


            }


        }
        int progress = (int)((((float)(pokemonInfo.currentHP))/pokemonInfo.maxHP)*100);

    }





}



