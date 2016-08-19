package com.example.user.myapplication.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.adapter.PokemonSearchListViewAdapter;
import com.example.user.myapplication.model.PokemonType;
import com.example.user.myapplication.model.SearchPokemonInfo;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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
    DialogViewHolder dialogViewHolder = null;

    PokemonSearchListViewAdapter adapter;

    /*初始化 Fragment*/
    public static PokemonSearchFragment newInstance() {

        Bundle args = new Bundle();
        PokemonSearchFragment fragment = new PokemonSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*初始化*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setMenuVisibility(false); //控制此menu 要顯示<看到menu就可以搜尋>還是隱藏<還沒取資料的時候UI元件是空的>

        //設定adapter 將 searchResult的內容 套用在 search_list_view 的 layout中
        adapter = new PokemonSearchListViewAdapter(getActivity(),R.layout.search_list_view,searchResult,this);

        //Read Data by query
        PokemonType.getQuery().getFirstInBackground(new GetCallback<PokemonType>() {
            @Override
            public void done(PokemonType object, ParseException e) {
                if (e == null){ //是否發生 Error
                    typeList = object.getTypeArray(); //抓到屬性列表
                    if (typeList != null) {
                        setMenuVisibility(true); //顯示上方icon
                        if (dialogViewHolder != null) { //避免非同步執行回傳空值
                            dialogViewHolder.setTypeList(0, typeList);
                            dialogViewHolder.setTypeList(1, typeList);
                        }
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

    ////////////////                 外觀設定                                    ////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //套用 layout 形成搜尋前的view
        if (fragmentView == null){
            fragmentView = inflater.inflate(R.layout.fragment_search,container,false);
            listView = (ListView)fragmentView.findViewById(R.id.listView);
            infoText = (TextView)fragmentView.findViewById(R.id.infoText);

            //Set list view adapter
            listView.setAdapter(adapter);
        }

        //是否看見 infoText
        hideOrShowInfoText(searchResult);

        //Dialog View
        //創建 SearchDialog
        if(searchDialog == null ){
            View dialogView = inflater.inflate(R.layout.search_form, null);
            dialogViewHolder = new DialogViewHolder(dialogView);//初始化dialog_form View

            //避免非同步進行的動作產生時差, 需要多一個判斷
            if (typeList != null ){
                dialogViewHolder.setTypeList(0,typeList);
                dialogViewHolder.setTypeList(1,typeList);
            }

            //dialog_form 的 view 外觀
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

    //設定 Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_fragment_menu,menu);
    }

    //點擊menu按鈕 顯示 dialog 搜尋框
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search){
            searchDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////               Dialog & ViewHolder 設計                     ////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    //Dialog ViewHolder
    public static class DialogViewHolder {

        //創立物件
        View dialogView;

        CheckBox[] conditionBoxs = new CheckBox[3];

        EditText nameText;

        CheckBox leftIntervalBox;
        CheckBox rightIntervalBox;
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

            nameText = (EditText)dialogView.findViewById(R.id.nameText);

            leftIntervalBox = (CheckBox)dialogView.findViewById(R.id.leftIntervalConditionBox);
            rightIntervalBox = (CheckBox)dialogView.findViewById(R.id.rightIntervalConditionBox);
            leftIntervalText = (EditText)dialogView.findViewById(R.id.leftInterval);
            rightIntervalText = (EditText)dialogView.findViewById(R.id.rightInterval);

            typeSelectors[0] = (Spinner)dialogView.findViewById(R.id.type1Selector);
            typeSelectors[1] = (Spinner)dialogView.findViewById(R.id.type2Selector);

        }

        //從layout 拿值

        //1. 要設定多少選擇條件
        public boolean conditionIsUsed(int index){
            if( index < 3 ){
                return conditionBoxs[index].isChecked();
            }else{
                return false;
            }
        }

        //2. 名字欄位
        public String getInputName(){
            return nameText.getText().toString();
        }

        //3. 是否篩選左右邊界
        public boolean containedByLeftInterval(){
            return leftIntervalBox.isChecked();
        }

        public boolean containedByRightInterval(){
            return rightIntervalBox.isChecked();
        }

        //4. 左右邊界的值
        public float getLeftIntervalVal(){
            return Float.valueOf(leftIntervalText.getText().toString());
        }

        public float getRightIntervalVal(){
            return Float.valueOf(rightIntervalText.getText().toString());
        }

        //5. 選擇的屬性 index (-1為 none)
        public int getSelectorType( int typeIndex ){
            int selectPos = typeSelectors[typeIndex].getSelectedItemPosition();
            if (selectPos ==0){
              return -1;
            }else{
                return selectPos-1;
            }
        }

        //6. 設定spinner 在index = 0 的地方加上 "none"屬性
        public void setTypeList( int typeIndex, ArrayList<String> typeList ){

            if( !typeList.contains("none") ){
                typeList.add(0, "none");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogView.getContext(),
                    android.R.layout.simple_spinner_item,
                    typeList); // 官方提供的adapter

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            typeSelectors[typeIndex].setAdapter(adapter); //屬性選擇欄位使用這個adapter呈現
        }
    }

    //Dialog click 的實作 function
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == AlertDialog.BUTTON_POSITIVE){
            starSearching();
        }
    }

    ////////////////                Searching & 結果呈現                         ////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    //Searching Function
    public void starSearching(){

        //連線資料庫 & 找到資料後要呼叫 callback function
        ParseQuery<SearchPokemonInfo> query = SearchPokemonInfo.getQuery();
        CustomizedFindCallback findCallback = new CustomizedFindCallback(searchResult,this);

        //搜尋 相符的 name
        if (dialogViewHolder.conditionIsUsed(0)){
            query = query.whereContains(SearchPokemonInfo.nameKey, dialogViewHolder.getInputName());
        }

        //搜尋 hp 範圍相符的
        if (dialogViewHolder.conditionIsUsed(1)){
            //是否採計左邊界
            if (dialogViewHolder.containedByLeftInterval()){
                query = query.whereGreaterThan(SearchPokemonInfo.hpKey, dialogViewHolder.getLeftIntervalVal());
            }

            //是否採計右邊界
            if (dialogViewHolder.containedByLeftInterval()){
                query = query.whereLessThan(SearchPokemonInfo.hpKey, dialogViewHolder.getRightIntervalVal());
            }
        }

        //搜尋 特定屬性
        if (dialogViewHolder.conditionIsUsed(2)){

            ArrayList<Integer> typeCondition = new ArrayList<>();

            for(int i = 0; i < 2; i++ ){
                int selectType = dialogViewHolder.getSelectorType(i);
                if (selectType == -1 ){
                    typeCondition.add(selectType);
                }
            }

            findCallback.numTypesInCondition = typeCondition.size(); //用 callback 去判斷到底設定幾個屬性搜尋
            query = query.whereContainsAll(SearchPokemonInfo.typeKey,typeCondition);
        }

        //
        query.findInBackground(findCallback);
    }

    //客製化 callback class
    private static class  CustomizedFindCallback implements FindCallback<SearchPokemonInfo>{

        public int numTypesInCondition = -1;
        private ArrayList<SearchPokemonInfo> searchResult;
        private PokemonSearchFragment searchFragment;

        CustomizedFindCallback( ArrayList<SearchPokemonInfo> resultBuffer, PokemonSearchFragment fragment ){
            searchResult = resultBuffer;
            searchFragment = fragment;
        }

        @Override
        public void done(List<SearchPokemonInfo> objects, ParseException e) {
            searchResult.clear();

            //判斷 搜尋的屬性數目 必須數目吻合 內容吻合才行
            if(numTypesInCondition != -1){
                for ( SearchPokemonInfo searchPokemonInfo : objects ){
                    //先將符合的結果取出全部的屬性
                    ArrayList<Integer> typeIndics = searchPokemonInfo.getTypeIndices();
                    //比對搜尋數目 相同才算是 searchResult
                    if (typeIndics.size() == numTypesInCondition){
                        searchResult.add(searchPokemonInfo);
                    }
                }
            }else{
                searchResult.addAll(objects);
            }

            //通知 ListView 進行改變_加入搜尋結果
            searchFragment.hideOrShowInfoText(searchResult);
            searchFragment.adapter.notifyDataSetChanged(); //更新 result View
        }
    }
}





