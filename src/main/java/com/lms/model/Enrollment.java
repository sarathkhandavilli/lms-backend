package com.lms.model;

import lombok.Data;

@Data
public class Enrollment {

    private int id;

    private String enrollId;

    private String enrollTime;

    private int courseId;

    private int learnerId;

    private String status;

    private double amount;

    private Integer paymentId;

}
