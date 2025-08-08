package com.lms.model;

import lombok.Data;

@Data
public class Payment {

    private int id;

    private String paymentId;

    private String nameOnCard;

    private String cvv;

    private String cardNo;

    private String expiryDate;

    private double amount;

    private String enrollmentId;

    private int learnerId;
}
