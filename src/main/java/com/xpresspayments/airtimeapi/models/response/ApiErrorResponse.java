package com.xpresspayments.airtimeapi.models.response;

import lombok.Data;

@Data
public class ApiErrorResponse {
    private final int errorCode;
    private final String errorMessage;

    public ApiErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
