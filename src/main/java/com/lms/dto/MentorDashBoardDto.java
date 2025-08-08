package com.lms.dto;

import lombok.Data;

@Data
public class MentorDashBoardDto {

    private long totalActiveCourses;

    private long totalDeletedCourses;

    private long totalCoursePurchases;

    private double totalSale;

}
