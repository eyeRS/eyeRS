package com.github.eyers;

/**
 * @deprecated needs to be removed or reworked
 */
@Deprecated
public class ItemInfo {

    private String symbol;
    private String name;
    private String sector;

    public ItemInfo(String symbol, String name, String sector) {
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getName() {
        return this.name;
    }

    public String getSector() {
        return this.sector;
    }

    @Override
    public String toString() {
        return "ItemInfo{" + "symbol=" + symbol + ", name=" + name + ", sector=" + sector + '}';
    }
}
