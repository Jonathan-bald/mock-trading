package com.stock.api.mock.trading.domain;

public class SymbolResponse {
    public String symbol;
    public Double price;
    public Double quantity;

    public SymbolResponse(String symbol, Double price, Double quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public SymbolResponse() {}
}
