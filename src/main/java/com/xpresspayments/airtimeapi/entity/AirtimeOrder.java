package com.xpresspayments.airtimeapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */

@Entity
@Table(name = "airtime_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirtimeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String responseId;
    private String phoneNo;
    private BigDecimal amount;
    private String status;
}
