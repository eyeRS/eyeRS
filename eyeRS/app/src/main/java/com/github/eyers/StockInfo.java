package com.github.eyers;

/**
 *
 */
public class StockInfo {

    private String symbol;
    private String name;
    private String sector;

    public StockInfo(String symbol, String name, String sector) {
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
        return "StockInfo{" + "symbol=" + symbol + ", name=" + name + ", sector=" + sector + '}';
    }
}
