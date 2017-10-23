package com.github.eyers;

import android.graphics.Bitmap;

/**
 * ItemLabel. Created by Matthew Van der Bijl on 2017/08/17.
 *
 * @author Matthew Van der Bijl
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

    /**
     * @return the name of the item
     */
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemLabel itemLabel = (ItemLabel) o;

        if (getName() != null ? !getName().equals(itemLabel.getName()) : itemLabel.getName() != null)
            return false;
        return getImage() != null ? getImage().equals(itemLabel.getImage()) : itemLabel.getImage() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getImage() != null ? getImage().hashCode() : 0);
        return result;
    }
}


