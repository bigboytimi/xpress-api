package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Data
public class TokenRequest {
    private String username;
    private String password;
}
