package com.xpresspayments.airtimeapi.repository;

import com.xpresspayments.airtimeapi.entity.AirtimeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */
public interface AirtimeOrderRepository extends JpaRepository<AirtimeOrder, Long> {

}
