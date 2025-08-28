package com.lms.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EnrollmentInfoMentorDto implements Serializable {
    
    private String learnerName;

    private String courseName;

    private double amount;

    private String enrolledTime;

    
}
