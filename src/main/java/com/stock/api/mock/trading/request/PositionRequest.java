package com.stock.api.mock.trading.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PositionRequest {

    @NotBlank(message = "Symbol cannot be blank.")
    public String symbol;
    @NotNull(message = "Quantity cannot be null.")
    public Double quantity;
}
