package com.android.prj.voicegame.publicClasses;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenRelative {

    public static int screenHeight;
    public static int screenWidth;

    public static void getScreenSize(Activity activity) {
        // get screen phone size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

         screenHeight = displayMetrics.heightPixels;
         screenWidth = displayMetrics.widthPixels;
    }
}
