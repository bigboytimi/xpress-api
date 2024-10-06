package com.xpresspayments.airtimeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.google.gson.Gson;
import com.xpresspayments.airtimeapi.controller.AirtimeOrderController;
import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.AirtimeDetails;
import com.xpresspayments.airtimeapi.models.request.AirtimePurchaseRequest;
import com.xpresspayments.airtimeapi.models.response.AirtimePurchaseResponse;
import com.xpresspayments.airtimeapi.service.AirtimeOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Objects;

@WebMvcTest(AirtimeOrderController.class)
class AirtimeOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AirtimeOrderService airtimeOrderService;

    @InjectMocks
    private AirtimeOrderController airtimeOrderController;

    AirtimeDetails details = new AirtimeDetails();
    AirtimePurchaseRequest request = new AirtimePurchaseRequest();

    @BeforeEach
    public void setup() {
        details.setPhoneNumber("08066572829");
        details.setAmount(BigDecimal.valueOf(100));

        request.setRequestId("2863626459836363");
        request.setUniqueCode("AIRTIME-101");
        request.setDetails(details);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuyAirtime_Success() throws Exception {

        Gson gson = new Gson();
        String jsonStringResponse = gson.toJson(request);

       AirtimePurchaseResponse response = AirtimePurchaseResponse.builder()
                .requestId("123454999283")
                .responseCode("00")
                .referenceId("1298284750545445")
               .responseMessage("Successful").data(null).build();

        when(airtimeOrderService.purchaseAirtime(any(AirtimePurchaseRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/api/v1/buyAirtime")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringResponse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode")
                        .value("00"));
    }

    @Test
    void testBuyAirtime_Failure() throws Exception {
        Gson gson = new Gson();
        String jsonStringResponse = gson.toJson(request);

        when(airtimeOrderService.purchaseAirtime(any(AirtimePurchaseRequest.class)))
                .thenThrow(new ApiException(500, "Internal Server Error"));

        mockMvc.perform(post("/api/v1/buyAirtime")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringResponse))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException))
                .andExpect(result -> assertEquals("Internal Server Error", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
