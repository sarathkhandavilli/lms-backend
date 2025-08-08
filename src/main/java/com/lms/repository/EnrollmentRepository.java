package com.lms.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lms.dto.EnrollmentInfoDto;
import com.lms.dto.EnrollmentInfoLearnerDto;
import com.lms.dto.EnrollmentInfoMentorDto;
import com.lms.model.Enrollment;

@Repository
public interface EnrollmentRepository {

    Enrollment add(Enrollment enrollment);

    List<EnrollmentInfoDto> findAll();

    List<EnrollmentInfoLearnerDto> findAllByLearnerId(int learnerId);

    boolean checkEnrollment(int courseId, int userId);

    List<EnrollmentInfoMentorDto> findAllEnrollmentsForMentor(int mentorId);
    
}