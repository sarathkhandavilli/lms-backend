package com.lms.repository;

import org.springframework.stereotype.Repository;

import com.lms.model.MentorDetail;

@Repository
public interface MentorDetailRepository {
    
    MentorDetail addMentorDetail(MentorDetail mentorDetail);
    MentorDetail findById(int id);
}
