package com.xpresspayments.airtimeapi.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */

@Entity
@Table(name = "airtime_orders")
@Data
public class AirtimeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String responseId;
    private String phoneNo;
    private String status;
}
