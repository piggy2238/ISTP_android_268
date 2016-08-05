package com.example.user.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.user.myapplication.model.Utils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by User on 2016/8/5.
 */
public class DrawerActivity extends AppCompatActivity {

    //宣告變數
    Toolbar toolbar;
    IProfile profile;
    AccountHeader headerResult;


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
}
