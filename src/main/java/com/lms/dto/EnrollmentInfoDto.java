package com.lms.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EnrollmentInfoDto implements Serializable{

    private String learnerName;

    private String mentorName;

    private String role;

    private String courseName;

    private String enrollmentId;

    private String paymentId;

    private double amountPaid;

    private String enrolledTime;
}
