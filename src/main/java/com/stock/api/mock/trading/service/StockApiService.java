package com.stock.api.mock.trading.service;

import com.stock.api.mock.trading.domain.StockApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url ="https://www.worldtradingdata.com/api/v1", name = "StockApiService")
public interface StockApiService {
    @RequestMapping(method = RequestMethod.GET, value = "/stock")
        StockApiResponse getSymbolInfo(@RequestParam("symbol") String symbol,@RequestParam("api_token") String apiToken);

}
