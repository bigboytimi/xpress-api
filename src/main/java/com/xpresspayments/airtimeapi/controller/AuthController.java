package com.xpresspayments.airtimeapi.controller;

import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.CreateUserRequest;
import com.xpresspayments.airtimeapi.models.request.TokenRequest;
import com.xpresspayments.airtimeapi.models.response.CreateUserResponse;
import com.xpresspayments.airtimeapi.models.response.TokenResponse;
import com.xpresspayments.airtimeapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "User Authentication API")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<CreateUserResponse> registerUser(@RequestBody @Valid CreateUserRequest request){
        try {
            //register service
            return userService.registerUser(request);
        }catch (Exception e){
            //throw custom exception in case an exception occurs
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PostMapping("/login")
    @Operation(summary = "Login User And Retrieve Token")
    public ResponseEntity<TokenResponse> loginAndGetToken(@RequestBody @Valid TokenRequest request){
        try {
            //login user to get jwt token
            return userService.validateAndReturnToken(request);
        }catch (Exception e){
            //throw custom exception in case an exception occurs
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
