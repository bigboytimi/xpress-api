package com.xpresspayments.airtimeapi.models.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */

@Data
public class CreateUserRequest {
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Username is required")
    private String userName;
    @NotEmpty(message = "Password is required")
    private String password;
}
