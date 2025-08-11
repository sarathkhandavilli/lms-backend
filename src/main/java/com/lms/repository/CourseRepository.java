package com.lms.repository;

import com.lms.dto.CourseDetailsDto;
import com.lms.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository {

    Optional<Course> findById(int id);
    Course save(Course course);

    // Optional<Course> findByIdAndUserId(int courseId, int userId);
    Optional<CourseDetailsDto> findCourseDetailsById(int courseId);
    Optional<CourseDetailsDto> findCourseDetailsByCourseIdAndUserId(int courseId, int userId);

    Optional<List<Course>> findByStatus(String status);
    List<Course> findByMentorAndStatus(int mentorId, String status);
    Optional<List<Course>> findByNameAndStatus(String courseName, String status);
    
    Long findCountByMentorAndStatus(int mentorId,String status);
    Long findCountPurchasedByMentor(int mentorId);
    List<Course> findCourseByCategory(int categoryId,String status);

    boolean deleteById(int id,int mentorId);
    boolean deactiveCourseByCategory(int categoryId);
    boolean deactiveCourseByMentor(int mentorId);
}
