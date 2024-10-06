package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Data
public class AirtimePurchaseRequest {
    @NotEmpty(message = "request id is required")
    private String requestId;
    @NotEmpty(message = "unique id is required")
    private String uniqueCode;
    @NotEmpty(message = "phone number is required")
    private AirtimeDetails details;
}
