package com.stock.api.mock.trading.dao;

import com.stock.api.mock.trading.domain.SymbolResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * We are implementing the spring class row mapper for JDBC template to use to map the row to our object
 */
public class PositionsRowMapper implements RowMapper<SymbolResponse> {
    //This will go through all the rows returned in the query and convert it to our StockApiResponse object
    @Override
    public SymbolResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        // In our result set we will have access to all columns returned in the SQL query (symbol, price) and we will convert this into a our object symbol response
        long id = rs.getLong("id");
        String symbol = rs.getString("symbol");
        Double price = rs.getDouble("price");
        Double quantity = rs.getDouble("quantity");
        return new SymbolResponse(id, symbol, price, quantity);
    }
}
