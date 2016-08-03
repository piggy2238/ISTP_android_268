package com.example.user.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.model.PokemonInfo;
import com.squareup.picasso.Picasso;

/**
 * Created by user on 2016/7/28.
 */
public class PokemonDetailActivity extends CustomizedActivity {

    PokemonInfo mpokemonInfo;
    Resources mRes;
    String packageName;
    String level;

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

//////////////////////////////////////////////////////////////////
    //Add Action Bar
    //1.加到頁面中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //透過menu Inflater 來生成對應的menu的id　|--- 新增的xml名字 ---|
        getMenuInflater().inflate(R.menu.pokemon_detail_action_bar_menu,menu);
        return true; // 一定會顯示menu
    }

    //2.增加功能
    //新增listener的概念 描述當menu被點到的時候要做什麼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //先透過getItenId拿到itemID
        int itemID = item.getItemId();

        //判斷是否被選到
        //判斷是否與menu內存在id相同 (相同表示存在<return true>,不相同表示不存在<return false>)
        if (itemID == R.id.action_save){
            //test是否按鈕可work
            //Log.d("menuItem","action_save");

            //0.準備一個包裹將記錄回傳
            Intent intent = new Intent();

            //1.紀錄被選到的pokemon key與name
            intent.putExtra(PokemonInfo.nameKey,mpokemonInfo.name);
            setResult(PokemonListActivity.listRemove,intent);

            //2.刪除此pokemon資料 (Work on ListActivity)

            //3.結束本頁瀏覽
            finish(); //進入onDistroy 階段

            return true;

        }else if(itemID == R.id.action_level_up){
            //測試按鈕是否可work
            //Log.d("menuItem","action_level_up");
            //1.取回pokemon level 資料
            int level=Integer.valueOf(mpokemonInfo.level);

            //2.pokemon level up
             level+=1;

            //3.存回原始紀錄
            levelText.setText(String.valueOf(level));
            Toast.makeText(this,"恭喜升級",Toast.LENGTH_SHORT).show();

            //4.準備一個包裹將記錄回傳
            Intent intent = new Intent();

            //5.紀錄被選到的pokemon key與name
            intent.putExtra(PokemonInfo.nameKey,mpokemonInfo.name);
            setResult(PokemonListActivity.listLevelup,intent);

            //6.升級此pokemon資料 (Work on ListActivity)

            //7.結束本頁瀏覽
            finish(); //進入onDistroy 階段

            return true;

        }

        return false;

    }
}



