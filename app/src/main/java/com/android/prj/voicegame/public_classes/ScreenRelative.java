package com.android.prj.voicegame.public_classes;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

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


    public void makeViewResponsive(int width, int height, View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);
    }
}
