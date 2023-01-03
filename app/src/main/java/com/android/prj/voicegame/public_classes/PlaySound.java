package com.android.prj.voicegame.public_classes;

import android.content.Context;
import android.media.MediaPlayer;

public class PlaySound {

    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, int mediaSource){
        mediaPlayer = MediaPlayer.create(context, mediaSource);
        mediaPlayer.start();
    }

    public static void stopSound(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
