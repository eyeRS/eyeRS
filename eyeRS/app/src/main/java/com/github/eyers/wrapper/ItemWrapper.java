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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemWrapper that = (ItemWrapper) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getImage() != null ? !getImage().equals(that.getImage()) : that.getImage() != null)
            return false;
        return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getImage() != null ? getImage().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
