package com.android.prj.voicegame.publicClasses;

import android.util.Log;

public class LogGenerator {
    public static void printLog(String name, String message){
        boolean loopVar1 = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (loopVar1){
                    Log.d(name, message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }
}
