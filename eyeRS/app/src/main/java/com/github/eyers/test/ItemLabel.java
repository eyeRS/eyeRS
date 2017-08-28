package com.github.eyers.test;

/**
 * Created by Matthew Van der Bijl on 2017/08/16.
 */
public class ItemLabel {

    private final String name, symbol, sector;

    public ItemLabel(String name, String symbol, String sector) {
        this.name = name;
        this.symbol = symbol;
        this.sector = sector;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getSector() {
        return this.sector;
    }
}

