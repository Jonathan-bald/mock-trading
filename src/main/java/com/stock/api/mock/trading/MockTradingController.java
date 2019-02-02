package com.stock.api.mock.trading;

import com.stock.api.mock.trading.domain.StockApiResponse;
import com.stock.api.mock.trading.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockTradingController {
    @Autowired
    private MockTradingDao Dao;
    @Autowired
    private StockApiService StockService;
    @RequestMapping("/hello")
    public void helloWorld(@RequestParam(name = ("Message")) String Message) {
        System.out.print(StockService.getSymbolInfo("APPL", "Q64v5wN7ZmwadOpYWwAbHF61fXEMgpAjQJlDLpQ0KChJ4MqOkXcC38zsZ2iv"));

    }
}

//    REST CALLS:
//        Get all positions
//        Insert new position
//        Cash out of position
//        Get total bank amount