package com.xpresspayments.airtimeapi;

import com.google.gson.Gson;
import com.xpresspayments.airtimeapi.models.request.AirtimeDetails;
import com.xpresspayments.airtimeapi.models.request.AirtimePurchaseRequest;
import com.xpresspayments.airtimeapi.models.response.AirtimePurchaseResponse;
import com.xpresspayments.airtimeapi.repository.AirtimeOrderRepository;
import com.xpresspayments.airtimeapi.service.AirtimeOrderService;
import com.xpresspayments.airtimeapi.service.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

class AirtimeOrderServiceTest {

    @InjectMocks
    private AirtimeOrderService airtimeOrderService;

    @Mock
    private AirtimeOrderRepository airtimeOrderRepository;

    @Mock
    private ApiService apiService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);;
    }

    @Test
    void testPurchaseAirtime_ValidPhoneNumber_Success() {

        Gson gson = new Gson();
        AirtimePurchaseRequest request = new AirtimePurchaseRequest();

        String jsonString = gson.toJson(AirtimePurchaseResponse.builder()
                .requestId("12345")
                .responseCode("00")
                .referenceId("129828475054545918")
                .data(null));

        String billerApiUrl = "https://billerstest.xpresspayments.com:9610/airtime/fulfil";
        when(apiService.postRequest(billerApiUrl, request)).thenReturn(ResponseEntity.ok(jsonString));

        ResponseEntity<AirtimePurchaseResponse> response = airtimeOrderService.purchaseAirtime(request);


        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(airtimeOrderRepository, times(1)).save(any());
    }

    @Test
    void testPurchaseAirtime_InvalidPhoneNumber() {
        AirtimeDetails details = new AirtimeDetails();
        details.setPhoneNumber("2979-863-5283528632838");
        details.setAmount(BigDecimal.valueOf(100));

        AirtimePurchaseRequest request = new AirtimePurchaseRequest();
        request.setRequestId("2863626459836363");
        request.setUniqueCode("AIRTIME-101");
        request.setDetails(details);

        ResponseEntity<AirtimePurchaseResponse> response = airtimeOrderService.purchaseAirtime(request);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("02", Objects.requireNonNull(response.getBody()).getResponseCode());
    }

}
