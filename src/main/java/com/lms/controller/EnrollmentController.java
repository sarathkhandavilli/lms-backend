package com.lms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lms.dto.EnrollmentDto;
import com.lms.dto.EnrollmentInfoDto;
import com.lms.dto.EnrollmentInfoLearnerDto;
import com.lms.dto.EnrollmentInfoMentorDto;
import com.lms.service.EnrollmentService;
import com.lms.dto.CommonApiResponse;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    EnrollmentService enrollmentService;

    // adds learner payment details and marks enrolled.
    @PostMapping("/enroll")
    public ResponseEntity<CommonApiResponse> enroll(@RequestBody EnrollmentDto enrollDto) {
        return enrollmentService.enroll(enrollDto);
    }

    // fetches all the enrolled learners ( admin can see all enrolled learners)
    @GetMapping("fetch/all")
    public ResponseEntity<CommonApiResponse> fetchAllEnrollments() {
        CommonApiResponse response;
        List<EnrollmentInfoDto> enrollmentInfoDtos =  enrollmentService.fetchAllEnrollments();
        
        logger.info("Started to fetch all enrollments");

        if (enrollmentInfoDtos == null) {
            response = new CommonApiResponse(true, "INTERNAL_SERVER_ERROR", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (enrollmentInfoDtos.isEmpty()) {
            response = new CommonApiResponse(false, "No enrollments present", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            response = new CommonApiResponse(true, "Fetched all enrollments successfully", enrollmentInfoDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // fetches all enrolled courses for particular learner (learner can see my courses)
    @GetMapping("fetch/learner-wise")
    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentsByLearner(@RequestParam("learnerId") int learnerId) {
        CommonApiResponse response;

        List<EnrollmentInfoLearnerDto> enrollmentInfoLearnerDtos = enrollmentService.fetchAllEnrollmentsByLearner(learnerId);

        if (enrollmentInfoLearnerDtos.isEmpty()) {
            response = new CommonApiResponse(false, "No enrollments for user", enrollmentInfoLearnerDtos);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            response = new CommonApiResponse(true, "Fetched all enrollments for learner " + learnerId + " successfully", enrollmentInfoLearnerDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // fetches all enrolled coureses for particular mentor (mentor can see enrolled courses)
    @GetMapping("fetch/mentor-wise")
    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentForMentor(@RequestParam("mentorId") int mentorId) {

        CommonApiResponse response;

        List<EnrollmentInfoMentorDto> enrollmentInfoMentorDtos =  enrollmentService.fetchAllEnrollmentsForMentor(mentorId);

        if (enrollmentInfoMentorDtos == null) {
            response = new CommonApiResponse(true, "INTERNAL_SERVER_ERROR", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (enrollmentInfoMentorDtos.isEmpty()) {
            response = new CommonApiResponse(false, "No enrollments found for mentor " + mentorId, enrollmentInfoMentorDtos);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            response = new CommonApiResponse(true, "Fetched all enrollments for mentor " + mentorId + " successfully", enrollmentInfoMentorDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
