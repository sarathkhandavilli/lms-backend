package com.lms.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MentorDetail implements Serializable{

    private int id;

    private int age;

    private int experience;

    private String qualification;

    private String profession;

    private String profilePic;

    

}
