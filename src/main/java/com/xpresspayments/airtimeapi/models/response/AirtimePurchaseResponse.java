package com.xpresspayments.airtimeapi.models.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Data
@Builder
public class AirtimePurchaseResponse {
    private String requestId;
    private String referenceId;
    private String responseCode;
    private String responseMessage;
    private String data;
}
