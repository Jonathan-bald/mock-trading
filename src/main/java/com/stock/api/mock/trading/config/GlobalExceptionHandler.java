package com.stock.api.mock.trading.config;

import com.stock.api.mock.trading.domain.BadRequestResponse;
import com.stock.api.mock.trading.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public BadRequestResponse handleConflict(BadRequestException e) {
        return new BadRequestResponse(e.message);
    }
}
