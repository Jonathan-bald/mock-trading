package com.stock.api.mock.trading.dao;

import com.stock.api.mock.trading.domain.SymbolResponse;
import com.stock.api.mock.trading.request.PositionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MockTradingDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdcTemplate;

    //This will retrieve all the rows with symbol and price
    private static String GET_ALL_POSITIONS = "SELECT symbol, price, quantity FROM positions WHERE userId = :userId";

    private static String GET_POSITION_BY_SYMBOL = "SELECT symbol, price, quantity FROM positions WHERE userId = :userId AND symbol = :symbol";

    //Add new row into position table with symbol, price and quantity values
    private static String CREATE_NEW_POSITION = "INSERT INTO positions (symbol, price, quantity, userId) VALUES (:symbol, :price, :quantity, :userId)";

    private static String GET_BANK_ROLL_BY_USER_ID = "SELECT amountUsd FROM bankroll WHERE userId = :userId";

    private static String UPDATE_BANK_ROLL_FOR_USER_ID = "UPDATE bankroll SET amountUsd = :newAmount WHERE userId = :userId";

    private static String UPDATE_POSITION_QUANTITY_BY_SYMBOL = "UPDATE positions SET quantity = :quantity WHERE userId = :userId AND symbol = :symbol LIMIT 1";

    private static String DELETE_POSITION_BY_SYMBOL = "DELETE FROM positions WHERE userId = :userId AND symbol = :symbol LIMIT 1";

    public List<SymbolResponse> getAllPositions(String userId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);

        return namedParameterJdcTemplate.query(GET_ALL_POSITIONS, parameters, new PositionsRowMapper());
    }

    public void removePosition(String userId, PositionRequest positionRequest) {
        List<SymbolResponse> symbolResponses = getPositionBySymbol(userId, positionRequest.symbol);
        if(!CollectionUtils.isEmpty(symbolResponses)) {
            SymbolResponse currentPosition = symbolResponses.get(0);
            //If request quantity less than total quantity
            if(positionRequest.quantity < currentPosition.quantity) {
                //Lower quantity
                updatePositionQuantity(userId, positionRequest.symbol, (currentPosition.quantity - positionRequest.quantity));
                //If request quantity equal to total quantity
            } else if(positionRequest.quantity.equals(currentPosition.quantity)) {
                //Delete position
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("symbol", positionRequest.symbol);
                parameters.put("userId", userId);
                namedParameterJdcTemplate.update(DELETE_POSITION_BY_SYMBOL, parameters);
            }
        }
    }

    public void updatePositionQuantity(String userId, String symbol, double quantity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        parameters.put("userId", userId);
        parameters.put("quantity", quantity);

        namedParameterJdcTemplate.update(UPDATE_POSITION_QUANTITY_BY_SYMBOL, parameters);
    }

    public List<SymbolResponse> getPositionBySymbol(String userId, String symbol) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("symbol", symbol);

        return namedParameterJdcTemplate.query(GET_POSITION_BY_SYMBOL, parameters, new PositionsRowMapper());
    }

    public void createNewPosition(String userId, PositionRequest positionRequest, double price) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("symbol", positionRequest.symbol);
        parameters.put("price", price);
        parameters.put("quantity", positionRequest.quantity);
        parameters.put("userId", userId);

        namedParameterJdcTemplate.update(CREATE_NEW_POSITION, parameters);
    }

    public Double getBankRollByUserId(String userId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);

        return namedParameterJdcTemplate.queryForObject(GET_BANK_ROLL_BY_USER_ID, parameters, Double.class);
    }

    public void updateBankrollForUserId(String userId, double newBankRoll) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("newAmount", newBankRoll);

        namedParameterJdcTemplate.update(UPDATE_BANK_ROLL_FOR_USER_ID, parameters);
    }
}
