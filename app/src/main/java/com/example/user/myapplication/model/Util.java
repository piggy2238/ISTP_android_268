package com.example.user.myapplication.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;

/**
 * Created by User on 2016/8/5. 目的:建立一些可利用於drawable的素材連結
 */
public class Util {

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

}
