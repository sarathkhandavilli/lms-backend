package com.lms.dto;

import lombok.Data;

@Data
public class EnrollmentDto {

    private int courseId;

    private int learnerId;

    private int mentorId;

    private String type;

    private String cardNo;

    private String nameOnCard;

    private String cvv;

    private String expiryDate;

    private double amount;

}
