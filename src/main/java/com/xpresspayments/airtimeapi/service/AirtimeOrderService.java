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
    private ApiService apiService;

    private AirtimeOrderRepository airtimeOrderRepository;
    private static final String PHONE_NUMBER_REGEX = "^0\\d{10}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }

    public ResponseEntity<AirtimePurchaseResponse> purchaseAirtime(AirtimePurchaseRequest request) {

        String phoneNumber = request.getDetails().getPhoneNumber();
        if (!isValidPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body(AirtimePurchaseResponse.builder()
                    .requestId(request.getRequestId()).responseMessage(XpressConstants.INVALID_PHONE_NUMBER)
                    .responseCode("02").referenceId(null).data(null)
                    .build());
        }

        ResponseEntity<String> apiResponse = apiService.postRequest(billerApiUrl, request);

        HttpStatus statusCode = apiResponse.getStatusCode();
        AirtimePurchaseResponse response = parseObject(apiResponse.getBody());

        saveAirtimeOrder(request, response);

        return ResponseEntity.status(statusCode).body(response);
    }

    private void saveAirtimeOrder(AirtimePurchaseRequest request, AirtimePurchaseResponse response) {
        AirtimeOrder order = new AirtimeOrder();
        order.setOrderId(response.getRequestId());
        order.setStatus(XpressResponseCode.PENDING);
        order.setResponseId(response.getReferenceId() == null ? "": response.getReferenceId());
        order.setPhoneNo(request.getDetails().getPhoneNumber());
        order.setAmount(request.getDetails().getAmount());

        if (XpressResponseCode.SUCCESS_CODE.equals(response.getResponseCode())){
            order.setStatus(XpressResponseCode.SUCCESS);
       }else {
            order.setStatus(XpressResponseCode.FAILED);
        }

        airtimeOrderRepository.save(order);
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
