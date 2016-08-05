package com.example.user.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.myapplication.fragment.PokemonListFragment;
import com.example.user.myapplication.fragment.TestFragment;
import com.example.user.myapplication.model.Utils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;

/**
 * Created by User on 2016/8/5.
 * 第一階段 完成基本的drawer 顯示  (未標示何時建構,極為第一階段建構)
 * 第二階段 管理PokemonListFragment (將另外標示為第二階段)
 * 第三階段 完備整個drawer的功能
 */
public class DrawerActivity extends AppCompatActivity {

    //第一階段宣告變數
    Toolbar toolbar;
    IProfile profile;
    AccountHeader headerResult;
    Drawer drawer;
    //第二階段
    Fragment[] fragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // Set a Toolbar to replace the ActionBar.
        // so it would be laid below the drawer when the drawer comes out
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Profile
        Drawable profileIcon = Utils.getDrawble(this,R.drawable.profile3);
        profile =new ProfileDrawerItem()
                .withName("hime")
                .withEmail("b96601009@gmail.com")
                .withIcon(profileIcon);

        //Build Drawer
        buildDrawerHeader(false, savedInstanceState);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .inflateMenu(R.menu.drawer_menu)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        ///////////////////////////////////////////////////////////////////////////
        //第二階段
        /*架構Fragment 空間分配及內容
        * 第一層放PokemonListFragment
        * 第二層放TestFragment,內容為Fake 2
        * 第三層放TestFragment,內容為Fake 3*/

        fragment = new Fragment[3];
        fragment[0] = PokemonListFragment.newInstance();
        fragment[1] = TestFragment.newInstance("Fake 2");
        fragment[2] = TestFragment.newInstance("Fake 3");

        /*遵循task(先進後出的資料結構),將資料倒序放入FragmentManager中*/
        fragmentManager = getFragmentManager();
        for(int i=fragment.length-1; i>=0; i--){
            replaceWithFragment(fragment[i]);
        }
    }

/*建構Drawer中的header
*函式中的參數 1).決定header是否寬鬆. 2).是否保留上次登入資訊
*/
    public void buildDrawerHeader(boolean compact, Bundle saveInstanceState){

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(profile)
                .withSavedInstance(saveInstanceState)
                .build();
    }
    /*第二階段：動態操作Fragment 透過FragmentManager來啟動每一個Transaction
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
