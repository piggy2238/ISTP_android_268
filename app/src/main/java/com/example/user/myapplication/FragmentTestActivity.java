package com.example.user.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.myapplication.fragment.LogFragment;
import com.example.user.myapplication.fragment.TestFragment;

/**
 * Created by User on 2016/8/5.
 */
public class FragmentTestActivity extends AppCompatActivity implements View.OnClickListener{

    //宣告變數
    Fragment[] fragment;
    FragmentManager fragmentManager;

/*新增及初始化一些元件
* 1.綁定activity_layout<同時需要新增一個layout>
* 2.此layout要包含可以顯示fragment的 framelayout
* 3.新增View 物件 及listener
* */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        //新增button View、物件、Listener
        Button replaceF1Btn =(Button)findViewById(R.id.replace_fragment_1);
        replaceF1Btn.setOnClickListener(this);
        Button replaceF2Btn =(Button)findViewById(R.id.replace_fragment_2);
        replaceF2Btn.setOnClickListener(this);
        Button removeF2Btn =(Button)findViewById(R.id.remove_fragment_2);
        removeF2Btn.setOnClickListener(this);

        //新增兩頁的fragment
        fragment = new Fragment[2];
        fragment[0] = TestFragment.newInstance("fragment_1");
        ((LogFragment)fragment[0]).actualName = "F1";
        fragment[1] = TestFragment.newInstance("fragment_2");
        ((LogFragment)fragment[1]).actualName = "F2";

        //新增fragmentManager
        fragmentManager = getFragmentManager();
    }

/*製作點了button 以後 fragment的操作
* 因為動作重複所以直接寫成function:replaceWithFragment
* 也可以用一行表示:LINE #62
* */
    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if (viewId == R.id.replace_fragment_1){
            replaceWithFragment(fragment[0]);
        }else if (viewId == R.id.replace_fragment_2){
            replaceWithFragment(fragment[1]);
        }else if (viewId == R.id.remove_fragment_2){
            if(fragment[1].isAdded()){
                //Start fragManager//START:transaction//Operate//END:transaction
                fragmentManager.beginTransaction().remove(fragment[1]).commit();
            }

        }
    }

/*動態操作Fragment 透過FragmentManager來啟動每一個Transaction
* 將頁面顯示替換成Fragment
* 1.取得物件
* 2.操作想要做的動作 (replace),需要兩個參數:1).存在的fragID.2).新的fragID
* 3.是否記錄各個commit的順序 (與backbutton的操作有關)
* 4.任何變動都需要存入comit才會生效
* */
    private void replaceWithFragment (Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //Start: operate fragment in transaction, such as add,remove,replace,......
        transaction.replace(R.id.fragmentContainer,fragment);
        transaction.addToBackStack(null);
        //End
        transaction.commit();
    }

}
