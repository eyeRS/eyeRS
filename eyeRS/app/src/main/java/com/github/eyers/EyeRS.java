package com.github.eyers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * TODO: comment code.
 * <p>
 * Created by Matthew on 2017/06/19.
 */
public final class EyeRS {

    /**
     *
     */
    public static final String PREFS_NAME = "EyeRS";
    /**
     *
     */
    public static EyeRS app;
    /**
     *
     */
    private final SharedPreferences preferences;

    /**
     * @param context
     */
    public EyeRS(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    /**
     * TODO.
     *
     * @return <code>this</code> for chaining
     */
    @Deprecated
    public EyeRS log() {
        return this;
    }

    /**
     * @return
     */
    public SharedPreferences getPreferences() {
        return this.preferences;
    }
}
