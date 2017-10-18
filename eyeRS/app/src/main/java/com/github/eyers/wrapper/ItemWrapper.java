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
    private final String description;


    public ItemWrapper(String name, Bitmap image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ItemWrapper{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", description='" + description + '\'' +
                '}';
    }
}
