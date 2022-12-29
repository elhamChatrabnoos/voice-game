package com.android.prj.voicegame.publicClasses;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class PublicSetting {

    public static void hideActionBar(ActionBar actionBar){
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    // use just english language
    public static void setAppLanguage(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
