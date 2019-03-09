package com.stock.api.mock.trading.exception;

public class FundsInsufficientException extends Exception {
    public double fundsRequired;
    public double fundsNeeded;

    public FundsInsufficientException(double fundsRequired, double fundsNeeded) {
        this.fundsRequired = fundsRequired;
        this.fundsNeeded = fundsNeeded;
    }
}
