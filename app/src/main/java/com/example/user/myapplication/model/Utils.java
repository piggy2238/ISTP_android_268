package com.example.user.myapplication.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by User on 2016/8/5.
 * 目的:
 * 1.getDrawable建立一些可利用於drawable的素材連結
 * 2.loadSongFromAssets 應該是跟音樂相關的素材連結 function
 * 3.google map 素材連結開發: (RESTful API)
 * 3-1.從url 讀取資料 並儲存於 byte array 中
 * 3-2.將取得的資料 到 google server 取得 經緯度
 */
public class Utils {

    public static Drawable getDrawble(Context context, int drawableId){
        if (Build.VERSION.SDK_INT < 21){
            return context.getResources().getDrawable(drawableId);
        }else{
            return context.getResources().getDrawable(drawableId,null);
        }
    }

    public static MediaPlayer loadSongFromAssets(Context context, String fileName){
        MediaPlayer mediaPlayer = null;

        try{
            AssetFileDescriptor descriptor;
            descriptor = context.getAssets().openFd(fileName);

            if(descriptor != null){
                mediaPlayer = new MediaPlayer();

                long start = descriptor.getStartOffset();
                long end = descriptor.getLength();


                mediaPlayer.setDataSource(descriptor.getFileDescriptor(),start,end);
                mediaPlayer.prepareAsync();
            }
        }
        catch (Exception e){

        }
        return mediaPlayer;

    }

    public static byte[] urltoBytes(String urlString) {

        try {

            URL url = new URL(urlString);

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatLngFromAddress(String address){
        //RESTful API: 透過網址向server要資料
        //1. 先將輸入的地址轉換編碼
        try{
            address = URLEncoder.encode(address,"UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //2. 完成 須查詢的網址
        String apiURL = "http://maps.google.com/maps/api/geocode/json?address="+address; //設定網址

        //3. 得到資料<此時為 byte型態>
        byte[] data = Utils.urltoBytes(apiURL); //透過網址的 Byte 資料

        if (data == null){return null;}

        //4. 將 byte 轉成 JSON 檔
        String result = new String(data);

        try{
            JSONObject jsonObject = new JSONObject(result);

            if( jsonObject.getString("status").equals("OK") ){
                //先取得 物件<key>
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                //拿到值
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                return new double[] {lat, lng};

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
