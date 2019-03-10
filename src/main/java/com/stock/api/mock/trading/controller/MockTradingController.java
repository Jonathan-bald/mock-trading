package com.stock.api.mock.trading.controller;

import com.stock.api.mock.trading.domain.BankrollResponse;
import com.stock.api.mock.trading.domain.StockApiResponse;
import com.stock.api.mock.trading.domain.SymbolResponse;
import com.stock.api.mock.trading.exception.BadRequestException;
import com.stock.api.mock.trading.request.PositionRequest;
import com.stock.api.mock.trading.service.MockTradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MockTradingController {

    @Autowired
    private MockTradingService mockTradingService;

    @GetMapping("/positions")
    public StockApiResponse getAllPositions(@RequestParam(value = "userId") String userId) {
        return mockTradingService.getAllPositions(userId);
    }

    @PostMapping("/positions")
    public ResponseEntity<?> createNewPosition(@RequestParam(value = "userId") String userId, @Valid @RequestBody PositionRequest positionRequest) throws BadRequestException {
        SymbolResponse symbolResponse = mockTradingService.createNewPosition(userId, positionRequest);
        //Return successful created response with the position info
        return ResponseEntity.status(201).body(symbolResponse);
    }

    @GetMapping("/bankRoll")
    public ResponseEntity<?> createNewPosition(@RequestParam(value = "userId") String userId) throws BadRequestException {
        double totalBankRoll = 0;
        totalBankRoll = mockTradingService.getTotalBankRoll(userId);
        return ResponseEntity.ok(new BankrollResponse(totalBankRoll));
    }

    @DeleteMapping("/positions")
    public ResponseEntity<?> deleteExistingPosition(@RequestParam(value = "userId") String userId, @Valid @RequestBody PositionRequest positionRequest) throws BadRequestException {
        mockTradingService.deleteExistingPosition(userId, positionRequest);
        return ResponseEntity.ok(ResponseEntity.EMPTY);
    }
}