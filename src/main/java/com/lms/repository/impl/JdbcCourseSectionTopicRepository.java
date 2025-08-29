package com.lms.repository.impl;

import com.lms.model.CourseSectionTopic;
import com.lms.repository.CourseSectionTopicRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCourseSectionTopicRepository implements CourseSectionTopicRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public CourseSectionTopic add(CourseSectionTopic courseSectionTopic) {
        String sql = "INSERT INTO course_section_topic (topic_no,name,description,youtube_url,section_id) VALUES (?,?,?,?,?) RETURNING ID ";

        Integer generatedId = jdbcTemplate.queryForObject(sql,Integer.class,
            courseSectionTopic.getTopicNo(),
            courseSectionTopic.getName(),
            courseSectionTopic.getDescription(),
            courseSectionTopic.getYoutubeUrl(),
            courseSectionTopic.getSectionId()
        );

        if (generatedId != null) {
            courseSectionTopic.setId(generatedId);
        }

        return courseSectionTopic;
    }

    @Override
    public String findYoutubeUrl(int courseSectionTopicId) {

        String sql = "SELECT youtube_url FROM course_section_topic WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql,new Object[]{courseSectionTopicId},String.class
            );
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

}
