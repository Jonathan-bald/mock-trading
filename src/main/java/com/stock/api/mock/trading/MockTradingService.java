package com.stock.api.mock.trading;

import com.stock.api.mock.trading.dao.MockTradingDao;
import com.stock.api.mock.trading.domain.StockApiResponse;
import com.stock.api.mock.trading.domain.SymbolResponse;
import com.stock.api.mock.trading.exception.FundsInsufficientException;
import com.stock.api.mock.trading.exception.SymbolDoesNotExistException;
import com.stock.api.mock.trading.requests.PositionRequest;
import com.stock.api.mock.trading.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockTradingService {

    @Autowired
    private MockTradingDao mockTradingDao;
    @Autowired
    private StockApiService stockApiService;

    private static final String API_TOKEN = "Q64v5wN7ZmwadOpYWwAbHF61fXEMgpAjQJlDLpQ0KChJ4MqOkXcC38zsZ2iv";

    public StockApiResponse getAllPositions(String userId) {
        List<SymbolResponse> symbolResponseList = mockTradingDao.getAllPositions(userId);
        StockApiResponse stockApiResponse = new StockApiResponse();
        stockApiResponse.data = symbolResponseList;
        return stockApiResponse;
    }

    public SymbolResponse createNewPosition(String userId, PositionRequest positionRequest) throws FundsInsufficientException, SymbolDoesNotExistException {
        StockApiResponse stockApiResponse = stockApiService.getSymbolInfo(positionRequest.symbol, API_TOKEN);
        //If price is not found the symbol might not exist so we will return null and the controller will throw a bad request
        if(stockApiResponse.data == null) {
            throw new SymbolDoesNotExistException();
        }
        Double price = null;
        //We will check the stock API to find the price for the symbol
        for(SymbolResponse symbolResponse : stockApiResponse.data) {
            //Make sure the symbol in the stock API request matches the users request on our API
            if(symbolResponse.symbol.equals(positionRequest.symbol)) {
                //Set price variable to the price that matches
                price = symbolResponse.price;
            }
        }
        //If we get to here everything is considered good and we can create our positions
        double fundsRequired = price * positionRequest.quantity;
        //Retrieve how much money the user currently has
        Double fundsAvailable = mockTradingDao.getBankRollByUserId(userId);
        if(doesUserHaveSufficientFunds(userId, fundsAvailable, fundsRequired)) {
            //They have sufficient funds to create the new position
            mockTradingDao.createNewPosition(userId, positionRequest, price);
            //Remove funds required for new position from the users bankroll
            mockTradingDao.updateBankrollForUserId(userId, fundsAvailable - fundsRequired);
            //Create response confirming to the user the position was created
            return new SymbolResponse(positionRequest.symbol, price, positionRequest.quantity);
        } else {
            throw new FundsInsufficientException(fundsRequired, fundsAvailable);
        }
    }

    public Double getTotalBankRoll(String userId) {
        return mockTradingDao.getBankRollByUserId(userId);
    }

    private boolean doesUserHaveSufficientFunds(String userId, Double fundsAvailable, double fundsRequired) {
        //User not found
        if(fundsAvailable == null) {
            return false;
        }
        //Does the user have enough funds for this transaction?
        return fundsAvailable >= fundsRequired;
    }
}
