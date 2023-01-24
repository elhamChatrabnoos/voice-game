package com.android.prj.voicegame.public_classes;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class PlaySound {

    private static MediaPlayer mediaPlayer;
    private static int length = 0;
    private static boolean loopVar;

    public static void playSound(Context context, int mediaSource, boolean enableLooping){
        mediaPlayer = MediaPlayer.create(context, mediaSource);
        mediaPlayer.setLooping(enableLooping);
        mediaPlayer.start();
    }

    public static void stopSound(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public static void pauseSound(){
        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();
    }

    public static  void continueSound(){
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
    }

}
