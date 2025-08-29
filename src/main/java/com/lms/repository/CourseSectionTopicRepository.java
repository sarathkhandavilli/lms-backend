package com.lms.repository;

import com.lms.model.CourseSectionTopic;


import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionTopicRepository {

    CourseSectionTopic add(CourseSectionTopic courseSectionTopic);
    String findYoutubeUrl(int courseSectionTopicId);
}
