package com.lms.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MentorDashBoardDto implements Serializable {

    private long totalActiveCourses;

    private long totalDeletedCourses;

    private long totalCoursePurchases;

    private double totalSale;

}
