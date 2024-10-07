package com.xpresspayments.airtimeapi.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Timi Olowookere
 * @since 7 Oct 2024
 */
@Service
public interface ApiService {
    ResponseEntity<String> postRequest(String url, Object requestBody);
}
