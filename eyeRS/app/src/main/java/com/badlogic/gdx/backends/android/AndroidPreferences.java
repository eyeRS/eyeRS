/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.backends.android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Taken from LibGDX.
 */
// https://github.com/libgdx/libgdx/wiki/Preferences
// https://github.com/libgdx/libgdx/blob/master/backends/gdx-backend-android/src/com/badlogic/gdx/backends/android/AndroidPreferences.java
public class AndroidPreferences {

    private SharedPreferences sharedPrefs;
    private Editor editor;

    public AndroidPreferences(SharedPreferences preferences) {
        this.sharedPrefs = preferences;
    }

    public AndroidPreferences putBoolean(String key, boolean val) {
        edit();
        editor.putBoolean(key, val);
        return this;
    }

    public AndroidPreferences putInteger(String key, int val) {
        edit();
        editor.putInt(key, val);
        return this;
    }

    public AndroidPreferences putLong(String key, long val) {
        edit();
        editor.putLong(key, val);
        return this;
    }

    public AndroidPreferences putFloat(String key, float val) {
        edit();
        editor.putFloat(key, val);
        return this;
    }

    public AndroidPreferences putString(String key, String val) {
        edit();
        editor.putString(key, val);
        return this;
    }

    public AndroidPreferences put(Map<String, ?> vals) {
        edit();
        for (Entry<String, ?> val : vals.entrySet()) {
            if (val.getValue() instanceof Boolean)
                putBoolean(val.getKey(), (Boolean) val.getValue());
            if (val.getValue() instanceof Integer)
                putInteger(val.getKey(), (Integer) val.getValue());
            if (val.getValue() instanceof Long) putLong(val.getKey(), (Long) val.getValue());
            if (val.getValue() instanceof String) putString(val.getKey(), (String) val.getValue());
            if (val.getValue() instanceof Float) putFloat(val.getKey(), (Float) val.getValue());
        }
        return this;
    }

    public boolean getBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public int getInteger(String key) {
        return sharedPrefs.getInt(key, 0);
    }

    public long getLong(String key) {
        return sharedPrefs.getLong(key, 0);
    }

    public float getFloat(String key) {
        return sharedPrefs.getFloat(key, 0);
    }

    public String getString(String key) {
        return sharedPrefs.getString(key, "");
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPrefs.getBoolean(key, defValue);
    }

    public int getInteger(String key, int defValue) {
        return sharedPrefs.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sharedPrefs.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sharedPrefs.getFloat(key, defValue);
    }

    public String getString(String key, String defValue) {
        return sharedPrefs.getString(key, defValue);
    }

    public Map<String, ?> get() {
        return sharedPrefs.getAll();
    }

    public boolean contains(String key) {
        return sharedPrefs.contains(key);
    }

    public void clear() {
        edit();
        editor.clear();
    }

    public void flush() {
        if (editor != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply();
            } else {
                editor.commit();
            }
            editor = null;
        }
    }

    public void remove(String key) {
        edit();
        editor.remove(key);
    }

    private void edit() {
        if (editor == null) {
            editor = sharedPrefs.edit();
        }
    }
}