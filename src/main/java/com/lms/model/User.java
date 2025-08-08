package com.lms.model;

import lombok.Data;

@Data
public class User {

    private int id;

    private Double amount ;

    private String firstName;

    private String lastName;

    private String emailId;

    private String password;

    private String phoneNo;

    private String role;

    private String status;

    private int mentorDetailId;

    private MentorDetail mentorDetail;
}
