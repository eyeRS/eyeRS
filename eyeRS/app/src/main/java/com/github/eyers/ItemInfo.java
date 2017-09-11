package com.github.eyers;

/**
 * @deprecated needs to be removed or reworked
 */
@Deprecated
public class ItemInfo {

    private String name;

    public ItemInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}
