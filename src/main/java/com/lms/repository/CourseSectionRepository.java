package com.lms.repository;

import com.lms.model.CourseSection;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseSectionRepository {

    Optional<CourseSection> findById(int id);
    CourseSection add(CourseSection courseSection);

}
