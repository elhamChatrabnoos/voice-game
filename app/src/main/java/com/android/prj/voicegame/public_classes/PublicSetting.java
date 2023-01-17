package com.android.prj.voicegame.public_classes;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.Locale;

public class PublicSetting {

    public static void hideBars(Activity activity){
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // Add a listener to update the behavior of the toggle fullscreen button when
        // the system bars are hidden or revealed.
        activity.getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
            // You can hide the caption bar even when the other system bars are visible.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                        || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
                }
            }
            return view.onApplyWindowInsets(windowInsets);
        });
    }

    // use just english language
    public static void setAppLanguage(Resources resources) {
        Configuration configuration = resources.getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}
