package com.example.user.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
public class PokemonSearchFragment extends Fragment /*implements DialogInterface.OnClickListener*/ {

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
        if(searchDialog == null ){

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

    //    @Override
//    public void onClick(DialogInterface dialog, int which) {
//
//    }
}





