package com.lms.repository;

import com.lms.model.CourseSectionTopic;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionTopicRepository {

    CourseSectionTopic add(CourseSectionTopic courseSectionTopic);
    String findYoutubeUrl(int courseSectionTopicId);
    List<CourseSectionTopic> findBySectionId(int sectionId, boolean isUser);
}
