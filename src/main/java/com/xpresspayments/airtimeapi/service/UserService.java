package com.xpresspayments.airtimeapi.service;

import com.xpresspayments.airtimeapi.constants.XpressConstants;
import com.xpresspayments.airtimeapi.entity.User;
import com.xpresspayments.airtimeapi.models.request.CreateUserRequest;
import com.xpresspayments.airtimeapi.models.request.TokenRequest;
import com.xpresspayments.airtimeapi.models.response.CreateUserResponse;
import com.xpresspayments.airtimeapi.models.response.TokenResponse;
import com.xpresspayments.airtimeapi.repository.UserRepository;
import com.xpresspayments.airtimeapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User existingUser = userRepository.findByUsername(request.getUserName());

        CreateUserResponse response = new CreateUserResponse();
        if (existingUser != null) {
            response.setMessage(XpressConstants.USER_EXISTS);
            response.setUsername(existingUser.getUsername());
            ResponseEntity.badRequest().body(response);
        }

        User userToSave = User.builder()
                .username(request.getUserName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User user = userRepository.save(userToSave);

        response.setMessage(XpressConstants.USER_REGISTERED_SUCCESSFULLY);
        response.setUsername(user.getUsername());
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<TokenResponse> validateAndReturnToken(TokenRequest request) {
        TokenResponse response =  new TokenResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            response.setToken(null);
            response.setMessage(XpressConstants.TOKEN_GENERATION_FAILED);
            return ResponseEntity.status(401).body(response);
        }

        User user = userRepository.findByUsername(request.getUsername());

        if (user == null){
            response.setToken(null);
            response.setToken(XpressConstants.LOGIN_FAILED);
            return ResponseEntity.status(401).body(response);
        }

        String jwtToken = jwtUtil.generateToken(user.getUsername());

        response.setToken(jwtToken);
        response.setMessage(XpressConstants.REQUEST_SUCCESSFUL);
        return ResponseEntity.ok().body(response);
    }
}
