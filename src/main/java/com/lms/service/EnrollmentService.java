package com.lms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lms.dto.CommonApiResponse;
import com.lms.dto.EnrollmentDto;
import com.lms.dto.EnrollmentInfoDto;
import com.lms.dto.EnrollmentInfoLearnerDto;
import com.lms.dto.EnrollmentInfoMentorDto;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.Payment;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.PaymentRepository;
import com.lms.repository.UserRepository;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    UserRepository userRepository;


public ResponseEntity<CommonApiResponse> enroll(EnrollmentDto enrollmentDto) {

        CommonApiResponse response;

        if (enrollmentDto.getCourseId() == 0 || enrollmentDto.getLearnerId() == 0) {
            logger.warn("Missing course or learner ID. Enrollment failed: {}", enrollmentDto);
            response = new CommonApiResponse(false, "Missing course or learner ID", enrollmentDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            logger.info("Processing enrollment for learner ID: {} in course ID: {}", enrollmentDto.getLearnerId(), enrollmentDto.getCourseId());

            Course course = courseRepository.findById(enrollmentDto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            User learner = userRepository.findById(enrollmentDto.getLearnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User Not found"));

            if (!learner.getRole().equals("LEARNER")) {
                throw new ResourceNotFoundException("Learner Not Found");
            }

            User mentor = userRepository.findById(course.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

            String generatedPaymentId = UUID.randomUUID().toString();

            if ("FREE".equalsIgnoreCase(enrollmentDto.getType())) {
                generatedPaymentId = null;
            }

            String generatedEnrollmentId = "ENR-" + System.currentTimeMillis();

            Payment savedPayment = null;
            
            if (!"FREE".equalsIgnoreCase(enrollmentDto.getType())) {
    
                Payment payment = new Payment();
                payment.setAmount(enrollmentDto.getAmount());
                payment.setCardNo(enrollmentDto.getCardNo());
                payment.setCvv(enrollmentDto.getCvv());
                payment.setExpiryDate(enrollmentDto.getExpiryDate());
                payment.setNameOnCard(enrollmentDto.getNameOnCard());
                payment.setPaymentId(generatedPaymentId);
                payment.setEnrollmentId(generatedEnrollmentId);
                payment.setLearnerId(enrollmentDto.getLearnerId());

                savedPayment = paymentRepository.add(payment);
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setEnrollId(generatedEnrollmentId);
            enrollment.setEnrollTime(LocalDateTime.now().toString());
            enrollment.setStatus("ENROLLED");
            enrollment.setAmount(enrollmentDto.getAmount());
            enrollment.setCourseId(course.getId());
            enrollment.setLearnerId(learner.getId());

            enrollment.setPaymentId(savedPayment != null ? savedPayment.getId() : null );

            Enrollment savedEnrollment = enrollmentRepository.add(enrollment);

            double currentMentorAmount = mentor.getAmount() == null ? 0.0 : mentor.getAmount();
            double updatedMentorAmount = currentMentorAmount + enrollmentDto.getAmount();

            mentor.setAmount(updatedMentorAmount);
            userRepository.update(mentor);

            logger.info("Enrollment successful for learner ID: {} in course ID: {}", enrollmentDto.getLearnerId(), enrollmentDto.getCourseId());
            response = new CommonApiResponse(true, "Enrollment Successful", savedEnrollment);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            logger.error("Error during enrollment: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error during enrollment: {}", e.getMessage());
            response = new CommonApiResponse(false, "Failed to enroll: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    public ResponseEntity<CommonApiResponse> fetchAllEnrollments() {

        CommonApiResponse response;

        try {
            logger.info("Fetching all enrollments");

            List<EnrollmentInfoDto> enrollmentInfoDtos = enrollmentRepository.findAll();

            if (enrollmentInfoDtos.isEmpty()) {
                logger.warn("No enrollments found.");
                response = new CommonApiResponse(false, "No enrollments present", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response = new CommonApiResponse(true, "Fetched all enrollments successfully", enrollmentInfoDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while fetching all enrollments: {}", e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentsByLearner(int learnerId) {

        CommonApiResponse response;

        try {
            logger.info("Fetching all enrollments for learner ID: {}", learnerId);

            List<EnrollmentInfoLearnerDto> enrollmentInfoLearnerDtos = enrollmentRepository.findAllByLearnerId(learnerId);

            if (enrollmentInfoLearnerDtos.isEmpty()) {
                logger.warn("No enrollments found for learner ID: {}", learnerId);
                response = new CommonApiResponse(false, "No enrollments for user", enrollmentInfoLearnerDtos);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response = new CommonApiResponse(true, "Fetched all enrollments for learner " + learnerId + " successfully", enrollmentInfoLearnerDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while fetching enrollments for learner ID {}: {}", learnerId, e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> fetchAllEnrollmentsForMentor(int mentorId) {

        CommonApiResponse response;

        try {
            logger.info("Fetching all enrollments for mentor ID: {}", mentorId);

            User mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

            List<EnrollmentInfoMentorDto> enrollmentInfoMentorDtos = enrollmentRepository.findAllEnrollmentsForMentor(mentorId);

            if (enrollmentInfoMentorDtos.isEmpty()) {
                logger.warn("No enrollments found for mentor ID: {}", mentorId);
                response = new CommonApiResponse(false, "No enrollments found for mentor " + mentorId, enrollmentInfoMentorDtos);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response = new CommonApiResponse(true, "Fetched all enrollments for mentor " + mentorId + " successfully", enrollmentInfoMentorDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while fetching enrollments for mentor ID {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
