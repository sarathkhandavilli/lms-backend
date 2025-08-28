package com.lms.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CourseDetailsDto implements Serializable{

    private int courseId;
    private String name;
    private int mentorId;
    private String mentorName;
    private String description;
    private String prerequisite;
    private String authorCourseNote;
    private Double price;
    private Double discountInPercent;
    private String type;
    private String thumbnail;
    private List<CourseSectionDto> courseSectionDtos;
}
