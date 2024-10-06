package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Data
public class TokenRequest {

    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
}
