package com.lms.dto;

import lombok.Data;

@Data
public class EnrollmentInfoLearnerDto {

    private String courseName;
    private int courseId;
    private double amountPaid;
    private String paymentId;
    private String enrollmentId;
    private String enrollmentTime;

}
