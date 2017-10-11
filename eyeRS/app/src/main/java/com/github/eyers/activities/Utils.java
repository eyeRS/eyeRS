package com.github.eyers.activities;

/**
 * Created by Emilde on 10/10/2017.
 */

import android.app.Activity;
import android.content.Intent;

import com.github.eyers.R;

public class Utils {

    public final static int App_Theme = 0;
    public final static int AppTheme_Red = 1;
    public final static int AppTheme_Yellow = 2;
    public final static int AppTheme_Green = 3;
    private static int sTheme;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case App_Theme:
                activity.setTheme(R.style.AppTheme);
                break;
            case AppTheme_Red:
                activity.setTheme(R.style.AppThemeRed);
                break;
            case AppTheme_Yellow:
                activity.setTheme(R.style.AppThemeYellow);
                break;
            case AppTheme_Green:
                activity.setTheme(R.style.AppThemeGreen);
        }
    }
}