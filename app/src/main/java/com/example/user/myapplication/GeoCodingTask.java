package com.example.user.myapplication;

import android.os.AsyncTask;

import com.example.user.myapplication.model.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

/**
 * Created by User on 2016/8/24.
 * <傳進去的參數 作progress update的參數 backgrouptask做完之後要return的型態>
 * 1. 宣告變數、建構子
 * 2. 背景執行接收經緯度
 * 3. 將經緯度結果傳給建構子 (callback 執行)
 */
public class GeoCodingTask extends AsyncTask<String, Void, double[]>{

    //用 弱reference 管理記憶體空間
    WeakReference<GeoCodingResponse> geoCodingResponseWeakReference;

    //Constructor
    public GeoCodingTask(GeoCodingResponse geoCondingResponse){
        geoCodingResponseWeakReference = new WeakReference<GeoCodingResponse>(geoCondingResponse);
    }

    //called on other Thread
    //接收經緯度資料, 較耗時因此再 background 執行
    @Override
    protected double[] doInBackground(String... params) {

        double[] latlng = Utils.getLatLngFromAddress(params[0]);

        if(latlng != null){
            return  latlng;
        }else{
            return null;
        }
    }

    //called on UI Thread after background task finished
    @Override
    protected void onPostExecute(double[] latlng) {
        super.onPostExecute(latlng);

        if( latlng != null ){

            LatLng result = new LatLng(latlng[0], latlng[1]); //存取經緯度

            if(geoCodingResponseWeakReference.get() != null ){
                geoCodingResponseWeakReference.get().callbackWithGeoCodingResult(result); //回傳result
            }
        }
    }

    //實體化
    public interface GeoCodingResponse{
        void callbackWithGeoCodingResult(LatLng latLng); //得到經緯度結果的對象
    }
}
