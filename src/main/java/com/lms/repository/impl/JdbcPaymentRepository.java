package com.lms.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lms.model.Payment;
import com.lms.repository.PaymentRepository;


@Repository
public class JdbcPaymentRepository implements PaymentRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Payment add(Payment payment) {
        String sql = "INSERT INTO payment (" +
            "amount, enrollment_id, card_no, cvv, expiry_date, name_on_card, payment_id, learner_id" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                payment.getAmount(),
                payment.getEnrollmentId(),
                payment.getCardNo(),
                payment.getCvv(),
                payment.getExpiryDate(),
                payment.getNameOnCard(),
                payment.getPaymentId(),
                payment.getLearnerId()
        );

        payment.setId(generatedId);
        return payment;
    }
}
