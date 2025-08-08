package com.lms.dto;

import lombok.Data;

@Data
public class EnrollmentInfoDto {

    private String learnerName;

    private String mentorName;

    private String courseName;

    private String enrollmentId;

    private String paymentId;

    private double amountPaid;

    private String enrolledTime;
}
