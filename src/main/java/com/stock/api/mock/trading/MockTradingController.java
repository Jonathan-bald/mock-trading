package com.stock.api.mock.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockTradingController {
    @Autowired
    private MockTradingDao Dao;
    @RequestMapping("/hello")
    public void helloWorld(@RequestParam(name = ("Message")) String Message) {
        System.out.print("Hello World");
        System.out.print(Message);

    }
}

//    REST CALLS:
//        Get all positions
//        Insert new position
//        Cash out of position
//        Get total bank amount