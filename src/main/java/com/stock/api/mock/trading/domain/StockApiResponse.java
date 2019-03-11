package com.stock.api.mock.trading.domain;

import java.util.List;

public class StockApiResponse {
    public List<SymbolResponse> data;

    public Double totalValue;

    @Override
    public String toString() {
        return "StockApiResponse{" +
                "data=" + data +
                '}';
    }
}
