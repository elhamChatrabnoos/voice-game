package com.android.prj.voicegame.customs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class CustomViews {

    public static void showText(String message, int targetX, int targetY, ConstraintLayout layout, Context context) {
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextSize(50);
        textView.setTextColor(Color.GREEN);
        textView.setX(targetX);
        textView.setY(targetY);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        layout.addView(textView);
        layout.postDelayed(() -> layout.removeView(textView), 1000);

    }

}
