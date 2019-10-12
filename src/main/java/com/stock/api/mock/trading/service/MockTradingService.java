package com.stock.api.mock.trading.service;

import com.stock.api.mock.trading.dao.MockTradingDao;
import com.stock.api.mock.trading.domain.StockApiResponse;
import com.stock.api.mock.trading.domain.SymbolResponse;
import com.stock.api.mock.trading.exception.BadRequestException;
import com.stock.api.mock.trading.request.PositionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MockTradingService {


    private MockTradingDao mockTradingDao;
    private StockApiService stockApiService;

    @Autowired
    public MockTradingService(MockTradingDao mockTradingDao, StockApiService stockApiService) {
        this.mockTradingDao = mockTradingDao;
        this.stockApiService = stockApiService;
    }

    private static final String API_TOKEN = "Q64v5wN7ZmwadOpYWwAbHF61fXEMgpAjQJlDLpQ0KChJ4MqOkXcC38zsZ2iv";

    public StockApiResponse getAllPositions(String userId) {
        List<SymbolResponse> symbolResponseList = mockTradingDao.getAllPositions(userId);
        StockApiResponse response = new StockApiResponse();
        response.data = symbolResponseList;

        double totalValue = 0;
        //Loop through all positions
        for(SymbolResponse symbolResponse : symbolResponseList) {
            //Find current price of each position
            StockApiResponse stockApiResponse = stockApiService.getSymbolInfo(symbolResponse.symbol, API_TOKEN);
            //Add that with the users quantity to their total value
            totalValue = totalValue + (stockApiResponse.data.get(0).price * symbolResponse.quantity);
        }
        response.totalValue = roundToTwoDecimalPlaces(totalValue);
        return response;
    }

    public SymbolResponse createNewPosition(String userId, PositionRequest positionRequest) throws BadRequestException {
        StockApiResponse stockApiResponse = stockApiService.getSymbolInfo(positionRequest.symbol, API_TOKEN);
        //If price is not found the symbol might not exist so we will return null and the controller will throw a bad request
        if(stockApiResponse.data == null) {
            throw new BadRequestException("Could not find symbol:" + positionRequest.symbol);
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
        double fundsAvailable = getTotalBankRoll(userId);
        if(doesUserHaveSufficientFunds(userId, fundsAvailable, fundsRequired)) {
            //They have sufficient funds to create the new position
            long positionId = mockTradingDao.createNewPosition(userId, positionRequest, price);
            //Remove funds required for new position from the users bankroll
            mockTradingDao.updateBankrollForUserId(userId, fundsAvailable - fundsRequired);
            //Create response confirming to the user the position was created
            return new SymbolResponse(positionId, positionRequest.symbol, price, positionRequest.quantity);
        } else {
            throw new BadRequestException("Bankroll is not sufficient. Funds required: " + fundsRequired + " Funds available: " + fundsAvailable);
        }
    }

    public void deleteExistingPosition(String userId, PositionRequest positionRequest) throws BadRequestException {
        StockApiResponse stockApiResponse = stockApiService.getSymbolInfo(positionRequest.symbol, API_TOKEN);
        List<SymbolResponse> symbolResponses = mockTradingDao.getPositionBySymbol(userId, positionRequest.symbol);
        if(!CollectionUtils.isEmpty(symbolResponses) && stockApiResponse.data != null) {
            //Get the position the user has in our database
            SymbolResponse symbol = symbolResponses.get(0);
            if(symbol.quantity >= positionRequest.quantity) {
                //Get the actual price from the API
                SymbolResponse realTimeSymbol = stockApiResponse.data.get(0);
                double currentBankRoll = getTotalBankRoll(userId);
                mockTradingDao.updateBankrollForUserId(userId, currentBankRoll + (positionRequest.quantity * realTimeSymbol.price));
                mockTradingDao.removePosition(userId, positionRequest);
            } else {
                throw new BadRequestException("You have an insufficient quantity for this transaction. Quantity requested: " + positionRequest.quantity + " Quantity held: " + symbol.quantity);
            }
        } else {
            throw new BadRequestException("Could not find symbol:" + positionRequest.symbol);
        }
    }

    public double getTotalBankRoll(String userId) throws BadRequestException {
        Double totalBankRoll = mockTradingDao.getBankRollByUserId(userId);
        if(totalBankRoll != null) {
            return roundToTwoDecimalPlaces(totalBankRoll);
        }
        throw new BadRequestException("Cannot find bankroll for user " + userId);
    }

    private boolean doesUserHaveSufficientFunds(String userId, Double fundsAvailable, double fundsRequired) {
        //Does the user have enough funds for this transaction?
        return fundsAvailable >= fundsRequired;
    }

    private static double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
