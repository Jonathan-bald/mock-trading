package com.stock.api.mock.trading.domain;

public class SymbolResponse {
    public long id;
    public String symbol;
    public Double price;
    public Double quantity;

    public SymbolResponse(long id, String symbol, Double price, Double quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public SymbolResponse() {}
}
