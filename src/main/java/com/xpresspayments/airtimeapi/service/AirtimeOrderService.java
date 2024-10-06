package com.xpresspayments.airtimeapi.service;

import com.google.gson.Gson;
import com.xpresspayments.airtimeapi.constants.XpressConstants;
import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.AirtimePurchaseRequest;
import com.xpresspayments.airtimeapi.models.response.AirtimePurchaseResponse;
import com.xpresspayments.airtimeapi.repository.AirtimeOrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author Timi Olowookere
 * @since 5 Oct 2024
 */
@Service
@RequiredArgsConstructor
public class AirtimeOrderService {


    @Value("${billerApi.url}")
    private String billerApiUrl;
    private ApiService apiService;

    private AirtimeOrderRepository airtimeOrderRepository;
    private static final String PHONE_NUMBER_REGEX = "^\\+?[0-9]{10,15}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }

    public ResponseEntity<AirtimePurchaseResponse> purchaseAirtime(AirtimePurchaseRequest request) {

        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(AirtimePurchaseResponse.builder()
                    .orderId(request.getOrderId())
                    .description(XpressConstants.INVALID_PHONE_NUMBER)
                    .build());
        }

        ResponseEntity<String> apiResponse = apiService.postRequest(billerApiUrl, request);

        if (!apiResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok().body(AirtimePurchaseResponse.builder()
                    .orderId(request.getOrderId())
                    .description(XpressConstants.RECHARGE_FAILED)
                    .build());
        }

        AirtimePurchaseResponse response = parseObject(apiResponse.getBody());
        return ResponseEntity.ok().body(response);
    }

    private AirtimePurchaseResponse parseObject(String body) {
        Gson gson = new Gson();
        try{
            return gson.fromJson(body, AirtimePurchaseResponse.class);
        }catch (Exception e){
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
