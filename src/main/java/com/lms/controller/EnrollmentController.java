package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lms.dto.EnrollmentDto;
import com.lms.service.EnrollmentService;
import com.lms.dto.CommonApiResponse;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

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
        return enrollmentService.fetchAllEnrollments(); 
    }

    // fetches all enrolled courses for particular learner (learner can see my courses)
    @GetMapping("fetch/learner-wise")
    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentsByLearner(@RequestParam("learnerId") int learnerId) {
        return enrollmentService.fetchAllEnrollmentsByLearner(learnerId);
    }

    // fetches all enrolled coureses for particular mentor (mentor can see enrolled courses)
    @GetMapping("fetch/mentor-wise")
    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentForMentor(@RequestParam("mentorId") int mentorId) {
        return enrollmentService.fetchAllEnrollmentsForMentor(mentorId);
    }
}
