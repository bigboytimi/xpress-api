package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Data
public class AirtimePurchaseRequest {
    private String orderId;
    private String phoneNumber;
}
