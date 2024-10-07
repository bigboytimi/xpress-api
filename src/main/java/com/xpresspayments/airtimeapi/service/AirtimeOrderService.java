package com.xpresspayments.airtimeapi.service;

import com.google.gson.Gson;
import com.xpresspayments.airtimeapi.constants.XpressConstants;
import com.xpresspayments.airtimeapi.constants.XpressResponseCode;
import com.xpresspayments.airtimeapi.entity.AirtimeOrder;
import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.AirtimePurchaseRequest;
import com.xpresspayments.airtimeapi.models.response.AirtimePurchaseResponse;
import com.xpresspayments.airtimeapi.repository.AirtimeOrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private ApiServiceImpl apiServiceImpl;

    private AirtimeOrderRepository airtimeOrderRepository;
    private static final String PHONE_NUMBER_REGEX = "^0\\d{10}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }

    public ResponseEntity<AirtimePurchaseResponse> purchaseAirtime(AirtimePurchaseRequest request) {

        //check if phone number matches the pattern expected
        //if phone number does not match, return a bad request error
        String phoneNumber = request.getDetails().getPhoneNumber();
        if (!isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body(AirtimePurchaseResponse.builder()
                    .requestId(request.getRequestId()).responseMessage(XpressConstants.INVALID_PHONE_NUMBER)
                    .responseCode("02").referenceId(null).data(null)
                    .build());
        }

        //phone number matches so call external api
        ResponseEntity<String> apiResponse = apiServiceImpl.postRequest(billerApiUrl, request);

        //if response from external api is null or empty, treat as a timeout error
        if (apiResponse == null || !apiResponse.hasBody()) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(AirtimePurchaseResponse.builder()
                    .requestId(request.getRequestId()).responseMessage(XpressConstants.GATEWAY_TIMEOUT)
                    .responseCode("02").referenceId(null).data(null)
                    .build());
        }

        //parse string response from api partner into POJO, set transaction status and save transaction
        HttpStatus statusCode = apiResponse.getStatusCode();
        AirtimePurchaseResponse response = parseObject(apiResponse.getBody());

        saveAirtimeOrder(request, response);

        //return response with accurate status code and response
        return ResponseEntity.status(statusCode).body(response);
    }

    private void saveAirtimeOrder(AirtimePurchaseRequest request, AirtimePurchaseResponse response) {
        AirtimeOrder order = new AirtimeOrder();
        order.setOrderId(response.getRequestId());
        order.setStatus(XpressResponseCode.PENDING);
        order.setResponseId(response.getReferenceId() == null ? "" : response.getReferenceId());
        order.setPhoneNo(request.getDetails().getPhoneNumber());
        order.setAmount(request.getDetails().getAmount());

        //set transaction status in line with external api response code
        if (XpressResponseCode.SUCCESS_CODE.equals(response.getResponseCode())) {
            order.setStatus(XpressResponseCode.SUCCESS);
        } else {
            order.setStatus(XpressResponseCode.FAILED);
        }
        //save transaction in db
        airtimeOrderRepository.save(order);
    }

    private AirtimePurchaseResponse parseObject(String body) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(body, AirtimePurchaseResponse.class);
        } catch (Exception e) {
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
