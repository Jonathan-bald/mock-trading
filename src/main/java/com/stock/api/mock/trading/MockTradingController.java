package com.stock.api.mock.trading;

import com.stock.api.mock.trading.dao.MockTradingDao;
import com.stock.api.mock.trading.domain.BadRequestResponse;
import com.stock.api.mock.trading.domain.BankrollResponse;
import com.stock.api.mock.trading.domain.StockApiResponse;
import com.stock.api.mock.trading.domain.SymbolResponse;
import com.stock.api.mock.trading.exception.FundsInsufficientException;
import com.stock.api.mock.trading.exception.SymbolDoesNotExistException;
import com.stock.api.mock.trading.requests.PositionRequest;
import com.stock.api.mock.trading.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MockTradingController {
    @Autowired
    private MockTradingDao Dao;
    @Autowired
    private StockApiService StockService;
    @Autowired
    private MockTradingService mockTradingService;

    @GetMapping("/positions")
    public StockApiResponse getAllPositions(@RequestParam(value = "userId") String userId) {
        return mockTradingService.getAllPositions(userId);
    }

    @PostMapping("/positions")
    public ResponseEntity<?> createNewPosition(@RequestParam(value = "userId") String userId, @Valid @RequestBody PositionRequest positionRequest) {
        try {
            SymbolResponse symbolResponse = mockTradingService.createNewPosition(userId, positionRequest);
            //Return successful created response with the position info
            return ResponseEntity.status(201).body(symbolResponse);
        } catch (FundsInsufficientException e) {
            return ResponseEntity.badRequest().body(new BadRequestResponse("Bankroll is not sufficient. Funds required: " + e.fundsRequired + " Funds available: " + e.fundsNeeded));
        } catch (SymbolDoesNotExistException e) {
            //Symbol was not found
            return ResponseEntity.badRequest().body(new BadRequestResponse("Could not find symbol: " + positionRequest.symbol));
        }
    }

    @GetMapping("/bankRoll")
    public ResponseEntity<?> createNewPosition(@RequestParam(value = "userId") String userId) {
        Double totalBankRoll = mockTradingService.getTotalBankRoll(userId);
        if (totalBankRoll == null) {
            return ResponseEntity.badRequest().body(new BadRequestResponse("Could not find bankroll for user: " + userId));
        }
        return ResponseEntity.ok(new BankrollResponse(totalBankRoll));
    }

    @DeleteMapping("/positions")
    public ResponseEntity<?> deletePosition(@RequestParam(value = "userId") String userId, @Valid @RequestBody PositionRequest positionRequest) {
        Double totalBankRoll = mockTradingService.getTotalBankRoll(userId);
        if (totalBankRoll == null) {
            return ResponseEntity.badRequest().body(new BadRequestResponse("Could not find bankroll for user: " + userId));
        }
        return ResponseEntity.ok(new BankrollResponse(totalBankRoll));
    }
}
//    REST CALLS:
//  Cash out of position
// 1: Call STOCK API to get todays price
// 2: Add todays price (price * quantity) to the users bankroll table
// 3: Delete the position in the positions table