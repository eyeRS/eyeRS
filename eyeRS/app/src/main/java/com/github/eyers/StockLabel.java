package com.github.eyers;

/**
 * Created by siD on 2017/08/17.
 */

public class StockLabel {

    private final String name, symbol, sector;

    public StockLabel(String name, String symbol, String sector) {
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
