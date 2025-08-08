package com.lms.dto;

import java.util.List;

import lombok.Data;

@Data
public class CourseDetailsDto {

    private int courseId;
    private int mentorId;
    private String name;
    private String description;
    private String prerequisite;
    private String authorCourseNote;
    private Double price;
    private Double discountInPercent;
    private String type;
    private String thumbnail;
    private List<CourseSectionDto> courseSectionDtos;
}
