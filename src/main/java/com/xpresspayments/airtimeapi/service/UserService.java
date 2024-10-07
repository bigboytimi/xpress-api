package com.xpresspayments.airtimeapi.service;

import com.xpresspayments.airtimeapi.constants.XpressConstants;
import com.xpresspayments.airtimeapi.entity.User;
import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.CreateUserRequest;
import com.xpresspayments.airtimeapi.models.request.TokenRequest;
import com.xpresspayments.airtimeapi.models.response.CreateUserResponse;
import com.xpresspayments.airtimeapi.models.response.TokenResponse;
import com.xpresspayments.airtimeapi.repository.UserRepository;
import com.xpresspayments.airtimeapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;


    public ResponseEntity<CreateUserResponse> registerUser(CreateUserRequest request) {

        //check if username exists
        Optional<User> existingUser = userRepository.findByUsername(request.getUserName());


        CreateUserResponse response = new CreateUserResponse();
        //if username exists, return bad request error
        if (existingUser.isPresent()) {
            response.setMessage(XpressConstants.USER_EXISTS);
            response.setUsername(existingUser.get().getUsername());
            ResponseEntity.badRequest().body(response);
        }

        //if username does not exist, build a new user entity and register with encoded password
        User userToSave = User.builder()
                .username(request.getUserName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User user = userRepository.save(userToSave);

        //return successful registration response
        response.setMessage(XpressConstants.USER_REGISTERED_SUCCESSFULLY);
        response.setUsername(user.getUsername());
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<TokenResponse> validateAndReturnToken(TokenRequest request) {
        TokenResponse response =  new TokenResponse();

        //authentication is managed by authenticationManager as specified in securityConfig
        // it authenticates the user and return jwt token. if error occurs, return api exception
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, XpressConstants.TOKEN_GENERATION_FAILED.concat("/nCause :: "+e.getMessage()));
        }

        //check if user exists
        Optional<User> user = userRepository.findByUsername(request.getUsername());

        //if user does not exist, return a login failed response
        if (!user.isPresent()){
            response.setToken(null);
            response.setToken(XpressConstants.LOGIN_FAILED);
            return ResponseEntity.status(401).body(response);
        }

        //if user exists, generate token for user
        String jwtToken = jwtUtil.generateToken(user.get().getUsername());

        //set response and successful status
        response.setToken(jwtToken);
        response.setMessage(XpressConstants.REQUEST_SUCCESSFUL);
        return ResponseEntity.ok().body(response);
    }
}
