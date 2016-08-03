package com.example.user.myapplication;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Handler;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;


public class MainActivity extends CustomizedActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,TextView.OnEditorActionListener {

//  設定使用變數:類別 名稱與內容
    TextView infoText;
    RadioGroup optionGrp;
    EditText name_editText;
    int selectedOptionIndex = 0;
    String[] pokemonNames = new String[]{
        "小火龍","傑尼龜","妙蛙種子"
    };
    ProgressBar progressBar;
//  程式初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//選擇要使用的Layout

        //透過id設定各Activity中的物件與xml的物件結合
        //如有需要根據各項目需求要設定其listener
        Button confirm_button = (Button)findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(this);

        optionGrp = (RadioGroup) findViewById(R.id.optionsGroup);
        optionGrp.setOnCheckedChangeListener(this);

        infoText = (TextView) findViewById(R.id.infoText);
        name_editText = (EditText) findViewById(R.id.name_editText);
        name_editText.setOnEditorActionListener(this);

        //Setting ProgressBar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(new CircularProgressDrawable
                .Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_color))
                .sweepSpeed(1f)
                .strokeWidth(8f)
                .build());
    }

///////////////////////////////////////////////////////////////////////////////////////////////
    //按下確認後
    //1. 顯示歡迎介面
    //2. 跳轉至下一頁面
    //  發生click事件要做的事情
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.confirm_button) {

            int changeActivityInSecs = 0; //延遲進入的秒數
            //1. 顯示歡迎介面
            infoText.setText(String.format("你好, 訓練家%s 歡迎來到神奇寶貝的世界,你的夥伴是%s,冒險即將於%d秒鐘之後開始",
                    name_editText.getText().toString(),
                    pokemonNames[selectedOptionIndex],
                    changeActivityInSecs
                    ));
            //2. 跳轉至下一頁面 //切換到另一個Activity
            //新增一個跳轉的物件
                Handler handler = new Handler(MainActivity.this.getMainLooper());
//              handler.postDelayed("Runnable","delay time")
//              handler.postDelayed("要做的事情","延遲 n 毫秒")
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //從Main Activity跳到PokemonListActivity去
                        Intent intent = new Intent(MainActivity.this,PokemonListActivity.class);
                        startActivity(intent);
                    //砍掉MainActivity 讓ListActivity 成為新的Root Activity
                    //跳轉後結束MainActivity
                    MainActivity.this.finish();
                }
            } , changeActivityInSecs * 1000);
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////
    //選擇夥伴的步驟
    //1.感受到checked/change
    //2.儲存選擇的項目
    //3.回傳選擇的項目,並設定預設值
    public final static String optionSelectedKey = "selection";

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int radioGrpID = group.getId();
        if(radioGrpID == R.id.optionsGroup) {
            switch(checkedId) {
                case R.id.option1:
                    selectedOptionIndex = 0;
                    break;
                case R.id.option2:
                    selectedOptionIndex = 1;
                    break;
                case R.id.option3:
                    selectedOptionIndex = 2;
                    break;
            }
        }
    }

    //UI狀態儲存
    @Override
    public void onSaveInstanceState(Bundle outState) {
       //Bundle 的類別 用put放東西 是以key value的形式儲存資料
        super.onSaveInstanceState(outState);
        outState.putInt(optionSelectedKey, selectedOptionIndex);
        //記下使用者所選擇的index
    }

    //回復使用者所選擇的index
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
                                                                       //預設值為0
        selectedOptionIndex = savedInstanceState.getInt(optionSelectedKey,0);
        ((RadioButton)optionGrp.getChildAt(selectedOptionIndex)).setChecked(true);
    }

///////////////////////////////////////////////////////////////////////////////////////////
    //測試不同Activity的功能, 以Log.d測試
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("testStage", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("testStage", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("testStage", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("testStage", "onDestroy");
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
