package com.example.user.myapplication;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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
    Button confirm_button;
    int changeActivityInSecs = 3; //延遲進入的秒數
    ProgressBar progressBar;

    //利用SharePreference 進行偏好設定, 以下為所需變數
    SharedPreferences preferences;
    String nameOfTheTrainer;
    public final static String optionSelectedKey = "selection";
    public final static String nameEditTextKey = "nameOfTheTrainer";

    //Setting UI 狀態
    public enum UISetting{Initial, DataIsKnown}
    UISetting uiSetting;

//  程式初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//選擇要使用的Layout

        //透過id設定各Activity中的物件與xml的物件結合
        //如有需要根據各項目需求要設定其listener
        confirm_button = (Button)findViewById(R.id.confirm_button);
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

        //判定是否為第一次的使用者
        //1.抓入偏好設定資料
        //2.抓偏好設定資料中_訓練家姓名
        //3.抓偏好設定資料中_第一隻神奇寶貝名字
        //4.判定是否為第一次使用者 (根據訓練家姓名是否為null為依據)
        //5.UI 狀態設定好以後再呼叫新的function 決定螢幕顯示狀況
        preferences = getSharedPreferences(Application.class.getName(),MODE_PRIVATE);
        selectedOptionIndex = preferences.getInt(optionSelectedKey,selectedOptionIndex);
        nameOfTheTrainer = preferences.getString(nameEditTextKey,nameOfTheTrainer);
        if(nameOfTheTrainer == null){
            uiSetting = UISetting.Initial;
        }else{
            uiSetting = UISetting.DataIsKnown;
        }
        changeUIAccordingToRecord();

    }

 //////////////////////////////////////////////////////////////////////////////////////////////
// 根據不同的登入情況 選擇UI要顯示及隱藏的項目
    private void changeUIAccordingToRecord(){
        if(uiSetting == UISetting.DataIsKnown){
         //隱藏組
           name_editText.setVisibility(View.INVISIBLE);
           optionGrp.setVisibility(View.INVISIBLE);
           confirm_button.setVisibility(View.INVISIBLE);
         //顯示組
            progressBar.setVisibility(View.VISIBLE);
            //although button is invisible, we can still simulate the button clicked.
            confirm_button.performClick();
        }else{
         //隱藏的顯示,顯示的隱藏
            name_editText.setVisibility(View.VISIBLE);
            optionGrp.setVisibility(View.VISIBLE);
            confirm_button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        //測試是否成功進入App
        //Toast.makeText(this,"這是首頁",Toast.LENGTH_LONG).show();
    }

///////////////////////////////////////////////////////////////////////////////////////////////
// 根據不同的登入狀況 選擇要顯示的文字
    private void setInfoTextWithFormat(){
        //針對原使用者
        if(uiSetting == UISetting.DataIsKnown){
            infoText.setText(String.format("你好, 訓練家%s 歡迎回到神奇寶貝的世界,你的夥伴是%s,冒險即將於%d秒鐘之後繼續",
                    nameOfTheTrainer,
                    pokemonNames[selectedOptionIndex],
                    changeActivityInSecs));

        }else{
            infoText.setText(String.format("你好, 訓練家%s 歡迎來到神奇寶貝的世界,你的夥伴是%s,冒險即將於%d秒鐘之後開始",
                    name_editText.getText().toString(),
                    pokemonNames[selectedOptionIndex],
                    changeActivityInSecs
            ));

        }
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
            //-1.點了button 以後不要再讓人家按
            v.setClickable(false);
            //0.判斷若為初次使用的使用者, 須將其資料儲存
            if(uiSetting == UISetting.Initial){
                nameOfTheTrainer = name_editText.getText().toString();
                SharedPreferences.Editor editor = preferences.edit();
                //紀錄訓練家姓名
                editor.putString(nameEditTextKey,nameOfTheTrainer);
                //紀錄選擇的神奇寶貝
                editor.putInt(optionSelectedKey,selectedOptionIndex);
                //紀錄為版本,成功存入
                editor.commit();
                Toast.makeText(this,"使用者資料已存入",Toast.LENGTH_LONG).show();
                //點確認後,把介面用好看一點
                //隱藏組
                name_editText.setVisibility(View.INVISIBLE);
                optionGrp.setVisibility(View.INVISIBLE);
                confirm_button.setVisibility(View.INVISIBLE);
                //顯示組
                progressBar.setVisibility(View.VISIBLE);

            }


            //1. 顯示歡迎介面
            setInfoTextWithFormat();

            //2. 跳轉至下一頁面 //切換到另一個Activity
            //新增一個跳轉的物件
            Handler handler = new Handler(MainActivity.this.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,PokemonListActivity.class);
                    intent.putExtra(optionSelectedKey,selectedOptionIndex);
                    startActivity(intent);
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
