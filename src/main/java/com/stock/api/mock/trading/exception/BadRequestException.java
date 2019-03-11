package com.stock.api.mock.trading.exception;

public class BadRequestException extends Exception {
    public String message;

    public BadRequestException(String message) {
        this.message = message;
    }
}
