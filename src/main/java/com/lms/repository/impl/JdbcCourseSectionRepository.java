package com.lms.repository.impl;

import com.lms.model.CourseSection;
import com.lms.repository.CourseSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcCourseSectionRepository implements CourseSectionRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<CourseSection> findById(int id) {
        String sql = "SELECT * FROM course_section WHERE id = ?";

        try {
            CourseSection courseSection = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                CourseSection courseSection1 = new CourseSection();
                courseSection1.setId(rs.getInt("id"));
                courseSection1.setName(rs.getString("name"));
                courseSection1.setSectionNo(rs.getString("section_no"));
                courseSection1.setDescription(rs.getString("description"));

                return courseSection1;
            },id);
            return Optional.of(courseSection);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public CourseSection add(CourseSection courseSection) {
        String sql = "INSERT INTO course_section (description, name, section_no, course_id) VALUES (?, ?, ?, ?) RETURNING id";

        Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                courseSection.getDescription(),
                courseSection.getName(),
                courseSection.getSectionNo(),
                courseSection.getCourseId()
                );

        if (generatedId != null) {
            courseSection.setId(generatedId);
        }

        return courseSection;
    }

}

