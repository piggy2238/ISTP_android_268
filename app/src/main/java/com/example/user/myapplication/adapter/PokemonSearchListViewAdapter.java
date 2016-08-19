package com.example.user.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myapplication.R;
import com.example.user.myapplication.fragment.PokemonSearchFragment;
import com.example.user.myapplication.model.SearchPokemonInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/8/19.
 */
public class PokemonSearchListViewAdapter extends ArrayAdapter<SearchPokemonInfo> {

    LayoutInflater inflater;
    int mRowViewLayoutId;
    PokemonSearchFragment searchFragment;


    public PokemonSearchListViewAdapter(Context context, int resource, List<SearchPokemonInfo> objects, PokemonSearchFragment fragment) {
        super(context, resource, objects);

        this.inflater = LayoutInflater.from(context);
        mRowViewLayoutId = resource;
        searchFragment = fragment;
    }

    //SearchResult
    public static class viewHolder{

        PokemonSearchListViewAdapter adapter;

        //宣告變數
        ImageView appearanceImg;
        TextView nameText;
        TextView hpText;
        TextView[] typeTexts = new TextView[2];

        //鍵結 layout
        viewHolder(View rowView, PokemonSearchListViewAdapter adapter){
            this.adapter = adapter;

            appearanceImg = (ImageView)rowView.findViewById(R.id.listImg);
            nameText = (TextView)rowView.findViewById(R.id.nameText);
            hpText = (TextView)rowView.findViewById(R.id.hpText);
            typeTexts[0] = (TextView)rowView.findViewById(R.id.type1Text);
            typeTexts[1] = (TextView)rowView.findViewById(R.id.type2Text);
        }

        //暫存 View 放入值
        public void setView(SearchPokemonInfo data){

            ImageLoader.getInstance().displayImage("http://www.csie.ntu.edu.tw/~r03944003/listImg/"+data.getPokedex()+".png",appearanceImg);
            nameText.setText(String.valueOf(data.getName()));
            hpText.setText(String.valueOf(data.gethp()));

            typeTexts[0].setText("");
            typeTexts[1].setText("");
            ArrayList<Integer> typeIndics = data.getTypeIndices();

            for(int i = 0; i<typeIndics.size(); i++){

                if(adapter.searchFragment.typeList != null ){

                    int typeIndex = typeIndics.get(i);

                    if( adapter.searchFragment.typeList.get(0).equals("none") ){
                        typeIndex++;
                    }

                    typeTexts[i].setText(adapter.searchFragment.typeList.get(typeIndex));
                }
            }
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchPokemonInfo dataItem = getItem(position);
        viewHolder viewHolder = null;

        if(convertView == null ){

            convertView = inflater.inflate(mRowViewLayoutId, parent, false);
            viewHolder = new viewHolder( convertView, this );
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (viewHolder)convertView.getTag();
        }

        viewHolder.setView(dataItem);
        return convertView;
    }
}
