package com.example.user.myapplication;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Handler;
import com.example.user.myapplication.model.OwningPokemonDataManager;
import com.example.user.myapplication.model.PokemonInfo;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

    TextView infoText;
    RadioGroup optionGrp;
    EditText name_editText;
    int selectedOptionIndex = 0;
    String[] pokemonNames = new String[]{
        "小火龍","傑尼龜","妙蛙種子"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button confirm_button = (Button)findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(this);

        optionGrp = (RadioGroup) findViewById(R.id.optionsGroup);
        optionGrp.setOnCheckedChangeListener(this);

        infoText = (TextView) findViewById(R.id.infoText);
        name_editText = (EditText) findViewById(R.id.name_editText);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.confirm_button) {

            int changeActivityInSecs = 5;
            infoText.setText(String.format("你好, 訓練家%s 歡迎來到神奇寶貝的世界,你的夥伴是%s,冒險即將於%d秒鐘之後開始",
                    name_editText.getText().toString(),
                    pokemonNames[selectedOptionIndex],
                    changeActivityInSecs
                    ));

            //切換到另一個Activity
                //延遲n秒進入
                //
                Handler handler = new Handler(MainActivity.this.getMainLooper());
//                handler.postDelayed("一個run的介面<function>","延遲 n 毫秒")
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //從Main Activity跳到PokemonListActivity去
                        //給予指令
                        Intent intent = new Intent(MainActivity.this,PokemonListActivity.class);
                        startActivity(intent);
                }
            } , changeActivityInSecs * 1000);
        }
    }

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


    public final static String optionSelectedKey = "selection";

    //儲存與回取資料
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

}
