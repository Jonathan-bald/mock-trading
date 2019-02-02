package com.stock.api.mock.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MockTradingDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdcTemplate;

//    public Double getPrice( String symbol ) {
//        // namedParameterJdcTemplate.query()
//
//    }
}
