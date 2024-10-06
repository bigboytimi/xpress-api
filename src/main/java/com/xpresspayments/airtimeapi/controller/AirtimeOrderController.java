package com.xpresspayments.airtimeapi.controller;

import com.xpresspayments.airtimeapi.exceptions.ApiException;
import com.xpresspayments.airtimeapi.models.request.AirtimePurchaseRequest;
import com.xpresspayments.airtimeapi.models.response.AirtimePurchaseResponse;
import com.xpresspayments.airtimeapi.service.AirtimeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Api(tags = "Airtime Operations")
public class AirtimeOrderController {
    private AirtimeOrderService airtimeOrderService;

    @PostMapping("/buyAirtime")
    @ApiOperation(value = "Purchase Airtime", notes = "Allows a user to purchase airtime using the provided details.")
    public ResponseEntity<AirtimePurchaseResponse> buyAirtime(@RequestBody @Valid AirtimePurchaseRequest request){
        try {
            return airtimeOrderService.purchaseAirtime(request);
        }catch (Exception e){
            throw new ApiException(Response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
