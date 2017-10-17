package com.github.eyers.wrapper;

import android.graphics.Bitmap;

/**
 * Item Wrapper. Created by Matthew Van der Bijl on 2017/10/17.
 *
 * @author Matthew Van der Bijl
 */
public class ItemWrapper {

    private final String name;
    private final Bitmap image;

    public ItemWrapper(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ItemWrapper{" +
                "name='" + name + '\'' +
                ", image=" + image +
                '}';
    }
}
