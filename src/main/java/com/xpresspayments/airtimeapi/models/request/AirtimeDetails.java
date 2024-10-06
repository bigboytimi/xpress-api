package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */
@Data
public class AirtimeDetails {
    @NotEmpty(message = "phone number is required")
    private String phoneNumber;
    @NotNull(message = "amount is required")
    private BigDecimal amount;
}
