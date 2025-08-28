package com.lms.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.CourseSection;
import lombok.Data;

@Data
public class CourseSectionDto implements Serializable{

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    private int courseId;

    private int mentorId;

    private String sectionNo;

    private String name;

    private String description;

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private List<CourseSectionTopicDto> courseSectionTopicDtos;

    public static CourseSection toEntity(CourseSectionDto courseSectionDto) {

        CourseSection courseSection = new CourseSection();

        courseSection.setId(courseSectionDto.getId());
        courseSection.setSectionNo(courseSectionDto.getSectionNo());
        courseSection.setName(courseSectionDto.getName());
        courseSection.setDescription(courseSectionDto.getDescription());
        courseSection.setCourseId(courseSectionDto.getCourseId());

        return courseSection;
    }

    public static CourseSectionDto toDto(CourseSection courseSection) {
        CourseSectionDto courseSectionDto = new CourseSectionDto();

        courseSectionDto.setId(courseSection.getId());
        courseSectionDto.setCourseId(courseSection.getCourseId());
        courseSectionDto.setSectionNo(courseSection.getSectionNo());
        courseSectionDto.setName(courseSection.getName());
        courseSectionDto.setDescription(courseSection.getDescription());
        courseSectionDto.setCourseId(courseSection.getCourseId());

        return courseSectionDto;
    }

}
