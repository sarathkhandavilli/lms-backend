package com.lms.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseSection {

    private int id;

    private int courseId;

    private String sectionNo;

    private String name;

    private String description;

    private List<CourseSectionTopic> courseSectionTopicList = new ArrayList<>();
}
