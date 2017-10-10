package com.github.eyers;

import android.graphics.Bitmap;

/**
 * Created by Matthew Van der Bijl on 2017/08/17.
 */
public class ItemLabel {

    private final String name;
    private final Bitmap image;

    public ItemLabel(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public Bitmap getImage() {
        return this.image;
    }
}
