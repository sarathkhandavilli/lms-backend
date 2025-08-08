package com.lms.repository;

import org.springframework.stereotype.Repository;
import com.lms.model.Payment;


@Repository
public interface PaymentRepository {

    Payment add(Payment payment);
    
}
