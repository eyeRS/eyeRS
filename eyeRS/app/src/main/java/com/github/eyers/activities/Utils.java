package com.github.eyers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.github.eyers.R;

/**
 * Created by Emilde on 10/10/2017.
 *
 * @author Emilde
 */
public class Utils {

    public final static int App_Theme = 0;
    public final static int AppTheme_Red = 1;
    public final static int AppTheme_Yellow = 2;
    public final static int AppTheme_Green = 3;

    public static int sTheme = App_Theme;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(AppCompatActivity activity, int theme) {
        sTheme = theme;
        // activity.setTheme(theme);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void onActivityCreateSetTheme(AppCompatActivity activity) {
        switch (sTheme) {
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
                break;
        }
    }

    public static int getTheme() {
        switch (sTheme) {
            case App_Theme:
                return (R.style.AppTheme);
            case AppTheme_Red:
                return (R.style.AppThemeRed);
            case AppTheme_Yellow:
                return (R.style.AppThemeYellow);
            case AppTheme_Green:
                return (R.style.AppThemeGreen);
            default:
                return -1;
        }
    }

}