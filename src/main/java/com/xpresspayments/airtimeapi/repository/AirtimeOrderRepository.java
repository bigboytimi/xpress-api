package com.xpresspayments.airtimeapi.repository;

import com.xpresspayments.airtimeapi.entity.AirtimeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */
@Repository
public interface AirtimeOrderRepository extends JpaRepository<AirtimeOrder, Long> {

}
