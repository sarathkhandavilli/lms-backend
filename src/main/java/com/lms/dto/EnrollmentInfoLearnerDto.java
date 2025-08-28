package com.lms.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EnrollmentInfoLearnerDto implements Serializable {

    private String courseName;
    private int courseId;
    private double amountPaid;
    private String paymentId;
    private String enrollmentId;
    private String enrollmentTime;

}
