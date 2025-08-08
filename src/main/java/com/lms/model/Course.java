package com.lms.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Course {

    private int id;

    private int mentorId;

    private int categoryId;

    private String name;

    private String description;

    private String type;

    private double price;

    private String addedDateTime;

    private String thumbnailName;

    private String status;

    private int discountInPercent;

    private String authorCourseNote;

    private String prerequisite;

    private List<CourseSection> sections = new ArrayList<>();
}
