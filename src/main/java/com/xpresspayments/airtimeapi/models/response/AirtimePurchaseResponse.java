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
    private String description;
    private String orderId;
    private String responseReference;
}
