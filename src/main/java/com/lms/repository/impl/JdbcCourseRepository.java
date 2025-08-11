package com.lms.repository.impl;

import com.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lms.dto.CourseDetailsDto;
import com.lms.dto.CourseSectionDto;
import com.lms.dto.CourseSectionTopicDto;
import com.lms.model.Course;

@Repository
public class JdbcCourseRepository implements CourseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<CourseDetailsDto> findCourseDetailsById(int courseId) {
    String sql = """
        SELECT c.id AS course_id, c.name AS course_name, c.description AS course_description, c.prerequisite, c.author_course_note,
               c.price, c.discount_in_percent, c.type, c.thumbnail,c.mentor_id,
               cs.id AS section_id, cs.section_no, cs.name AS section_name, cs.description AS section_description,
               cst.id AS topic_id, cst.topic_no, cst.name AS topic_name, cst.description AS topic_description
        FROM course c
        LEFT JOIN course_section cs ON cs.course_id = c.id
        LEFT JOIN course_section_topic cst ON cst.section_id = cs.id
        WHERE c.id = ?
    """;

    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId);

    if (rows.isEmpty()) {
        return Optional.empty();
    }

    CourseDetailsDto courseDetailsDto = new CourseDetailsDto();
    Map<String, CourseSectionDto> sectionMap = new LinkedHashMap<>();
    Map<String, CourseSectionTopicDto> topicMap = new LinkedHashMap<>();

    for (Map<String, Object> row : rows) {
        // Set course-level data only once
        if (courseDetailsDto.getName() == null) {
            courseDetailsDto.setCourseId((Integer) row.get("course_id"));
            courseDetailsDto.setName((String) row.get("course_name"));
            courseDetailsDto.setDescription((String) row.get("course_description"));
            courseDetailsDto.setPrerequisite((String) row.get("prerequisite"));
            courseDetailsDto.setAuthorCourseNote((String) row.get("author_course_note"));
            courseDetailsDto.setPrice(((BigDecimal) row.get("price")).doubleValue());
            courseDetailsDto.setDiscountInPercent(((Number) row.get("discount_in_percent")).doubleValue());
            courseDetailsDto.setType((String) row.get("type"));
            courseDetailsDto.setThumbnail((String) row.get("thumbnail"));
            courseDetailsDto.setMentorId((Integer) row.get("mentor_id"));
        }

        String sectionNo = (String) row.get("section_no");
        CourseSectionDto sectionDto = sectionMap.get(sectionNo);
        if (sectionDto == null) {
            sectionDto = new CourseSectionDto();
            sectionDto.setSectionNo(sectionNo);
            sectionDto.setName((String) row.get("section_name"));
            sectionDto.setDescription((String) row.get("section_description"));

            // Handle potential null value for section_id
            Integer sectionId = (Integer) row.get("section_id");
            if (sectionId != null) {
                sectionDto.setId(sectionId);
            }

            sectionDto.setCourseSectionTopicDtos(new ArrayList<>());
            sectionMap.put(sectionNo, sectionDto);
        }

        CourseSectionTopicDto topicDto = new CourseSectionTopicDto();

        if (topicMap.containsKey((String)row.get("topic_no"))) {
            continue;
        }

        topicDto.setTopicNo((String) row.get("topic_no"));
        topicDto.setName((String) row.get("topic_name"));
        topicDto.setDescription((String) row.get("topic_description"));

        // Handle potential null value for topic_id
        Integer topicId = (Integer) row.get("topic_id");
        if (topicId != null) {
            topicDto.setId(topicId);
        }

        sectionDto.getCourseSectionTopicDtos().add(topicDto);
        topicMap.put(topicDto.getTopicNo(),topicDto);
    }

    courseDetailsDto.setCourseSectionDtos(new ArrayList<>(sectionMap.values()));
    return Optional.of(courseDetailsDto);
}

    

    public Optional<CourseDetailsDto> findCourseDetailsByCourseIdAndUserId(int courseId, int userId) {
        String sql = """
            SELECT c.id as course_id, c.mentor_id, c.name AS course_name, c.description AS course_description, c.prerequisite, c.author_course_note,
                c.price, c.discount_in_percent, c.type, c.thumbnail,
                cs.id AS section_id, cs.section_no, cs.name AS section_name, cs.description AS section_description,
                cst.id as topic_id, cst.topic_no, cst.name AS topic_name, cst.description AS topic_description, cst.youtube_url
            FROM course c
            LEFT JOIN course_section cs ON cs.course_id = c.id
            LEFT JOIN course_section_topic cst ON cst.section_id = cs.id
            LEFT JOIN enrollment e ON e.course_id = c.id
            WHERE c.id = ? AND ( e.learner_id = ? or c.mentor_id = ? )
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId,userId,userId);

        if (rows.isEmpty()) return Optional.empty();

        CourseDetailsDto courseDetailsDto = new CourseDetailsDto();
        Map<String, CourseSectionDto> sectionMap = new LinkedHashMap<>();
        Map<String,CourseSectionTopicDto> topicMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            // Set course info once
            if (courseDetailsDto.getName() == null) {
                courseDetailsDto.setCourseId((Integer) row.get("course_id"));
                courseDetailsDto.setName((String) row.get("course_name"));
                courseDetailsDto.setDescription((String) row.get("course_description"));
                courseDetailsDto.setPrerequisite((String) row.get("prerequisite"));
                courseDetailsDto.setAuthorCourseNote((String) row.get("author_course_note"));
                courseDetailsDto.setPrice(((BigDecimal) row.get("price")).doubleValue());
                courseDetailsDto.setDiscountInPercent(((Number) row.get("discount_in_percent")).doubleValue());
                courseDetailsDto.setType((String) row.get("type"));
                courseDetailsDto.setThumbnail((String) row.get("thumbnail"));
                courseDetailsDto.setMentorId((Integer) row.get("mentor_id"));

            }

            String sectionNo = (String) row.get("section_no");
            CourseSectionDto sectionDto = sectionMap.get(sectionNo);
            if (sectionDto == null) {
                sectionDto = new CourseSectionDto();
                sectionDto.setSectionNo(sectionNo);
                sectionDto.setName((String) row.get("section_name"));
                sectionDto.setDescription((String) row.get("section_description"));

                // Handle potential null value for section_id
                Integer sectionId = (Integer) row.get("section_id");
                if (sectionId != null) {
                    sectionDto.setId(sectionId);
                }

                sectionDto.setCourseSectionTopicDtos(new ArrayList<>());
                sectionMap.put(sectionNo, sectionDto);
            }

            CourseSectionTopicDto topicDto = new CourseSectionTopicDto();
            if (topicMap.containsKey((String) row.get("topic_no"))) {
                continue;
            }
            topicDto.setTopicNo((String) row.get("topic_no"));
            topicDto.setName((String) row.get("topic_name"));
            topicDto.setDescription((String) row.get("topic_description"));
            topicDto.setYoutubeUrl((String) row.get("youtube_url"));

            // Handle potential null value for topic_id
            Integer topicId = (Integer) row.get("topic_id");
            if (topicId != null) {
                topicDto.setId(topicId);
            }

            sectionDto.getCourseSectionTopicDtos().add(topicDto);
            topicMap.put(topicDto.getTopicNo(),topicDto);
        }

        courseDetailsDto.setCourseSectionDtos(new ArrayList<>(sectionMap.values()));
        return Optional.of(courseDetailsDto);
    }


    public Optional<Course> findById(int id) {
        String sql = "SELECT * FROM course WHERE id = ?";

        try {
            Course course = jdbcTemplate.queryForObject(sql, (rs,rowNum) -> {
                Course course1 = new Course();
                course1.setId(rs.getInt("id"));
                course1.setAuthorCourseNote(rs.getString("author_course_note"));
                course1.setStatus(rs.getString("status"));
                course1.setPrice(rs.getDouble("price"));
                course1.setPrerequisite(rs.getString("prerequisite"));
                course1.setName(rs.getString("name"));
                course1.setType(rs.getString("type"));
                course1.setAddedDateTime(rs.getTimestamp("added_date_time").toString());
                course1.setDescription(rs.getString("description"));
                course1.setDiscountInPercent(rs.getInt("discount_in_percent"));
                course1.setMentorId(rs.getInt("mentor_id"));
                course1.setCategoryId(rs.getInt("category_id"));
                course1.setThumbnailName(rs.getString("thumbnail"));

                return course1;
            },id);
            return Optional.of(course);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Course save(Course course) {
        if (course.getId() != 0) {
            // Course already exists, update the thumbnail
            String updateSql = "UPDATE course SET thumbnail = ? WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(updateSql, course.getThumbnailName(), course.getId());
    
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to update course thumbnail. Course ID not found: " + course.getId());
            }
    
            return course;
        } else {
            // New course, insert and return generated ID
            String sql = "INSERT INTO course (" +
                    "name, description, prerequisite, author_course_note, price, discount_in_percent, " +
                    "type, thumbnail, status, category_id, mentor_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    
            Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                    course.getName(),
                    course.getDescription(),
                    course.getPrerequisite(),
                    course.getAuthorCourseNote(),
                    course.getPrice(),
                    course.getDiscountInPercent(),
                    course.getType().toUpperCase(),
                    course.getThumbnailName(),
                    course.getStatus(),
                    course.getCategoryId(),
                    course.getMentorId()
            );
    
            course.setId(generatedId);
            return course;
        }

    }
    // @Override
    // public Optional<Course> findByIdAndUserId(int courseId, int userId) {
    //     // TODO Auto-generated method stub
    //     return Optional.empty();
    // }
    @Override
    public Optional<List<Course>> findByStatus(String status) {
        String sql = "SELECT * FROM course WHERE status = ?";

        List<Course> courses = jdbcTemplate.query(sql, new Object[]{status}, (rs, rowNum) -> {
            Course course = new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setPrerequisite(rs.getString("prerequisite"));
            course.setAuthorCourseNote(rs.getString("author_course_note"));
            course.setPrice(rs.getDouble("price"));
            course.setDiscountInPercent(rs.getInt("discount_in_percent"));
            course.setAddedDateTime(rs.getTimestamp("added_date_time").toString());
            course.setType(rs.getString("type"));
            course.setThumbnailName(rs.getString("thumbnail"));
            course.setStatus(rs.getString("status"));
            course.setCategoryId(rs.getInt("category_id"));
            course.setMentorId(rs.getInt("mentor_id"));
            return course;
        });

        return courses.isEmpty() ? Optional.empty() : Optional.of(courses);

    }
    @Override
    public List<Course> findByMentorAndStatus(int mentorId, String status) {
        String sql = "SELECT * FROM course WHERE mentor_id = ? AND status = ?";

        return jdbcTemplate.query(sql, new Object[]{mentorId, status}, (rs, rowNum) -> {
            Course course = new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setPrerequisite(rs.getString("prerequisite"));
            course.setAuthorCourseNote(rs.getString("author_course_note"));
            course.setPrice(rs.getDouble("price"));
            course.setDiscountInPercent(rs.getInt("discount_in_percent"));
            course.setAddedDateTime(rs.getTimestamp("added_date_time").toString());
            course.setType(rs.getString("type"));
            course.setThumbnailName(rs.getString("thumbnail"));
            course.setStatus(rs.getString("status"));
            course.setCategoryId(rs.getInt("category_id"));
            course.setMentorId(rs.getInt("mentor_id"));
            return course;
        });
    }
    @Override
    public Optional<List<Course>> findByNameAndStatus(String courseName, String status) {

        courseName = "%" + courseName.replace(" ", "%") + "%";
        String sql = "SELECT * FROM course WHERE name ILIKE ? AND status = ?";

        List<Course> courses = jdbcTemplate.query(sql, new Object[]{courseName, status}, (rs, rowNum) -> {
            Course course = new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setPrerequisite(rs.getString("prerequisite"));
            course.setAuthorCourseNote(rs.getString("author_course_note"));
            course.setPrice(rs.getDouble("price"));
            course.setDiscountInPercent(rs.getInt("discount_in_percent"));
            course.setAddedDateTime(rs.getTimestamp("added_date_time").toString());
            course.setType(rs.getString("type"));
            course.setThumbnailName(rs.getString("thumbnail"));
            course.setStatus(rs.getString("status"));
            course.setCategoryId(rs.getInt("category_id"));
            course.setMentorId(rs.getInt("mentor_id"));
            return course;
        });

        return courses.isEmpty() ? Optional.empty() : Optional.of(courses);
    }
    @Override
    public boolean deleteById(int id,int mentorId) {
        String sql = "UPDATE course SET status = ? WHERE id = ? AND mentor_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, "INACTIVE", id,mentorId);
        return rowsAffected > 0;
    }
    @Override
    public Long findCountByMentorAndStatus(int mentorId, String status) {
        String sql = "SELECT COUNT(*) FROM course WHERE mentor_id = ? AND status = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{mentorId, status}, Long.class);
    }
    @Override
    public Long findCountPurchasedByMentor(int mentorId) {
            String sql = """
            SELECT COUNT(*)
            FROM payment p
            JOIN enrollment e ON p.enrollment_id = e.enrollment_id
            JOIN course c ON e.course_id = c.id
            WHERE c.status = 'ACTIVE'
            AND e.status = 'ENROLLED'
            AND c.mentor_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, Long.class, mentorId);
    }

    @Override
    public List<Course> findCourseByCategory(int categoryId, String status) {

        String sql = "SELECT * FROM course WHERE category_id = ? AND status = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Course course = new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setPrerequisite(rs.getString("prerequisite"));
            course.setAuthorCourseNote(rs.getString("author_course_note"));
            course.setPrice(rs.getDouble("price"));
            course.setDiscountInPercent(rs.getInt("discount_in_percent"));
            course.setAddedDateTime(rs.getTimestamp("added_date_time").toString());
            course.setType(rs.getString("type"));
            course.setThumbnailName(rs.getString("thumbnail"));
            course.setStatus(rs.getString("status"));
            course.setCategoryId(rs.getInt("category_id"));
            course.setMentorId(rs.getInt("mentor_id"));
            return course;
        },categoryId,status);
    }

    public boolean deactiveCourseByCategory(int categoryId) {

        String sql = "UPDATE course SET status = ? WHERE status = ? AND category_id = ?";

        int rowsAffected = jdbcTemplate.update(sql,"INACTIVE","ACTIVE",categoryId);

        return rowsAffected >= 0;

    }

    public boolean deactiveCourseByMentor(int mentorId) {

        String sql = "UPDATE course SET status = ? WHERE status = ? AND mentor_id = ?";

        int rowsAffected = jdbcTemplate.update(sql,"INACTIVE","ACTIVE",mentorId);

        return rowsAffected >= 0;

    }

}
