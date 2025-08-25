package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.Course;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseDto implements Serializable {
     
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    private int mentorId;

    private int categoryId;

    private String name;

    private String description;

    private String type;

    private double price;

    private int discountInPercent;

    private String authorCourseNote;

    private String prerequisite;

    private String thumbnailName;

    private MultipartFile thumbnail;

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private List<CourseSectionDto> courseSectionDtos;

    private String status;

    public static Course toEntity(CourseDto courseDto) {
        Course course = new Course();

        course.setId(courseDto.getId());
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setType(courseDto.getType().toUpperCase());
        course.setPrice(courseDto.getPrice());
        course.setDiscountInPercent(courseDto.getDiscountInPercent());
        course.setAuthorCourseNote(courseDto.getAuthorCourseNote());
        course.setPrerequisite(courseDto.getPrerequisite());
        course.setMentorId(courseDto.getMentorId());
        course.setCategoryId(courseDto.getCategoryId());
        return course;
    }

    public static CourseDto toDto(Course course) {

        CourseDto courseDto = new CourseDto();

        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());
        courseDto.setType(course.getType());
        courseDto.setPrice(course.getPrice());
        courseDto.setDiscountInPercent(course.getDiscountInPercent());
        courseDto.setAuthorCourseNote(course.getAuthorCourseNote());
        courseDto.setPrerequisite(course.getPrerequisite());
        courseDto.setMentorId(course.getMentorId());
        courseDto.setCategoryId(course.getCategoryId());
        courseDto.setThumbnailName(course.getThumbnailName());
        courseDto.setStatus(course.getStatus());

        return courseDto;
    }
}
