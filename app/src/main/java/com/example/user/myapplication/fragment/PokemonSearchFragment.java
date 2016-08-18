package com.example.user.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.model.PokemonType;
import com.example.user.myapplication.model.SearchPokemonInfo;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;

/**
 * Created by User on 2016/8/18.
 */
public class PokemonSearchFragment extends Fragment implements DialogInterface.OnClickListener {

    //變數
    AlertDialog searchDialog;
    View fragmentView;
    TextView infoText;

    public ArrayList<String> typeList = null;
    ArrayList<SearchPokemonInfo> searchResult = new ArrayList<>();

    ListView listView;
//    DialogViewHolder dialogViewHolder = null;


    /*初始化 Fragment*/
    public static PokemonSearchFragment newInstance() {

        Bundle args = new Bundle();
        PokemonSearchFragment fragment = new PokemonSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*Read data*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setMenuVisibility(false);

        //Read Data by query
        PokemonType.getQuery().getFirstInBackground(new GetCallback<PokemonType>() {
            @Override
            public void done(PokemonType object, ParseException e) {
                if (e == null){ //是否發生 Error
                    typeList = object.getTypeArray(); //抓到屬性列表
                    if (typeList != null) {
                        setMenuVisibility(true); //顯示上方icon
//                        if (dialogViewHolder != null) { //避免非同步執行回傳空值
//                            dialogViewHolder.setTypeList(0, typeList);
//                            dialogViewHolder.setTypeList(1, typeList);
//                        }
                    }else{
                        setMenuVisibility(false);
                        Toast.makeText(getActivity(), "沒抓到屬性資料, 確保網路是開啟的", Toast.LENGTH_LONG).show();
                    }
                }else{
                    setMenuVisibility(false);
                    Toast.makeText(getActivity(), "發生不明原因錯誤", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //套用 layout 形成物件
        if (fragmentView == null){
            fragmentView = inflater.inflate(R.layout.fragment_search,container,false);
            listView = (ListView)fragmentView.findViewById(R.id.listView);
            infoText = (TextView)fragmentView.findViewById(R.id.infoText);
            //TODO:Set list view adapter
        }

        //是否看見 infoText
        hideOrShowInfoText(searchResult);

        //Dialog View
        //創建 SearchDialog
        if(searchDialog == null ){
            View dialogView = inflater.inflate(R.layout.search_form, container, false);
            searchDialog = new AlertDialog.Builder(getActivity()).setView(dialogView)
                .setNegativeButton("取消", this)
                .setPositiveButton("搜尋", this)
                .create();
        }

        return fragmentView;
    }

    //是否看到 "請按右上鍵進行搜尋" 的 function
    public void hideOrShowInfoText(ArrayList<SearchPokemonInfo> result){
        if( infoText != null ){
            if(result.size() == 0){
                infoText.setVisibility(View.VISIBLE);
            }else{
                infoText.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Dialog click 的實作 function
        @Override
    public void onClick(DialogInterface dialog, int which) {

            if (which == AlertDialog.BUTTON_POSITIVE){
                starSearching();
            }else if ( which == AlertDialog.BUTTON_NEGATIVE ){

            }

    }

    //Searching Function
    public void starSearching(){

    }

    //Dialog ViewHolder
    public static class DialogViewHolder {

        //創立物件
        View dialogView;

        CheckBox[] conditionBoxs = new CheckBox[3];
        CheckBox leftIntervalBox;
        CheckBox rightIntervalBox;

        EditText nameText;
        EditText leftIntervalText;
        EditText rightIntervalText;

        Spinner[] typeSelectors = new Spinner[2];

        //Create Constructor 對應 layoutId
        DialogViewHolder(View dialogView){
            this.dialogView = dialogView;   //把inflate 生成的view 物件放入

            //建立 物件 與 layout id 的連結
            conditionBoxs[0] = (CheckBox)dialogView.findViewById(R.id.conditionbox1);
            conditionBoxs[1] = (CheckBox)dialogView.findViewById(R.id.conditionbox2);
            conditionBoxs[2] = (CheckBox)dialogView.findViewById(R.id.conditionbox3);

            leftIntervalBox = (CheckBox)dialogView.findViewById(R.id.leftIntervalConditionBox);
            rightIntervalBox = (CheckBox)dialogView.findViewById(R.id.rightIntervalConditionBox);

            nameText = (EditText)dialogView.findViewById(R.id.nameText);
            leftIntervalText = (EditText)dialogView.findViewById(R.id.leftInterval);
            rightIntervalText = (EditText)dialogView.findViewById(R.id.rightInterval);

            typeSelectors[0] = (Spinner)dialogView.findViewById(R.id.type1Selector);
            typeSelectors[1] = (Spinner)dialogView.findViewById(R.id.type2Selector);

        }
    }
}





