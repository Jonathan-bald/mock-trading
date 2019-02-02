package com.stock.api.mock.trading.domain;

public class SymbolResponse {
    public String symbol;
    public String price;

    @Override
    public String toString() {
        return "SymbolResponse{" +
                "symbol='" + symbol + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
