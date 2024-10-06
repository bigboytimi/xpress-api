package com.xpresspayments.airtimeapi.exceptions;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException {
    private final int errorCode;
    private final String errorMessage;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
