package com.lms.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lms.dto.EnrollmentInfoDto;
import com.lms.dto.EnrollmentInfoLearnerDto;
import com.lms.dto.EnrollmentInfoMentorDto;
import com.lms.model.Enrollment;
import com.lms.repository.EnrollmentRepository;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class JdbcEnrollmentRepository implements EnrollmentRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Enrollment add(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment (" +
            "amount, enrollment_id, enrollment_time, status, course_id, learner_id, payment_id" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                enrollment.getAmount(),
                enrollment.getEnrollId(),
                enrollment.getEnrollTime(),
                enrollment.getStatus(),
                enrollment.getCourseId(),
                enrollment.getLearnerId(),
                enrollment.getPaymentId()
        );

        enrollment.setId(generatedId);
        return enrollment;
    }

    public List<EnrollmentInfoDto> findAll() {

        String sql = """
                    SELECT
                        l.first_name || ' ' || l.last_name AS learner_name,
                        m.first_name || ' ' || m.last_name AS mentor_name,
                        c.name AS course_name,
                        e.enrollment_id,
                        p.payment_id,
                        e.amount AS amount_paid,
                        e.enrollment_time
                    FROM enrollment e
                    LEFT JOIN users l ON e.learner_id = l.id
                    JOIN course c ON e.course_id = c.id
                    JOIN users m ON c.mentor_id = m.id
                    LEFT JOIN payment p ON p.enrollment_id = e.enrollment_id
                    ORDER BY e.enrollment_time DESC
                """;

            List<EnrollmentInfoDto> enrollmentInfoDtos = jdbcTemplate.query(sql, (rs, rowNum) -> {
                EnrollmentInfoDto enrollmentInfoDto = new EnrollmentInfoDto();
                enrollmentInfoDto.setLearnerName(rs.getString("learner_name"));
                enrollmentInfoDto.setMentorName(rs.getString("mentor_name"));
                enrollmentInfoDto.setCourseName(rs.getString("course_name"));
                enrollmentInfoDto.setEnrollmentId(rs.getString("enrollment_id"));
                enrollmentInfoDto.setPaymentId(rs.getString("payment_id"));
                enrollmentInfoDto.setAmountPaid(rs.getDouble("amount_paid"));
                enrollmentInfoDto.setEnrolledTime(rs.getString("enrollment_time"));

                return enrollmentInfoDto;
            });

            return enrollmentInfoDtos;
    }

    public List<EnrollmentInfoLearnerDto> findAllByLearnerId(int learnerId,String userTimeZone) {

        String sql = """
                    SELECT
                        c.id as course_id,
                        c.name AS course_name,
                        e.amount AS amount_paid,
                        e.payment_id,
                        e.enrollment_id,
                        e.enrollment_time
                    FROM users l
                    JOIN enrollment e ON l.id = e.learner_id 
                    JOIN course c ON e.course_id = c.id 
                    WHERE l.id = ?
                """;

        List<EnrollmentInfoLearnerDto> enrollmentInfoLearnerDto = jdbcTemplate.query(sql,(rs,rowNum) -> {
            EnrollmentInfoLearnerDto dto = new EnrollmentInfoLearnerDto();
            dto.setCourseName(rs.getString("course_name"));
            dto.setAmountPaid(rs.getDouble("amount_paid"));
            dto.setPaymentId(rs.getString("payment_id"));
            dto.setEnrollmentId(rs.getString("enrollment_id"));

            String utcTimeString = rs.getString("enrollment_time");

            logger.info("enrollment time before changing "+utcTimeString+" "+userTimeZone);


            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.n");

            LocalDateTime localDateTime = LocalDateTime.parse(utcTimeString, dateTimeFormatter);

            Instant utcInstant = localDateTime.toInstant(ZoneOffset.UTC);

            ZoneId userZone = ZoneId.of(userTimeZone);

            ZonedDateTime userDateTime = utcInstant.atZone(userZone);

            String formattedTime = userDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a z"));

            logger.info("enrollment time after changing "+formattedTime);

            dto.setEnrollmentTime(formattedTime);
            dto.setCourseId(rs.getInt("course_id"));
            return dto;
        },learnerId);

        return enrollmentInfoLearnerDto;
    }

    public boolean checkEnrollment(int courseId, int userID) {

        String sql = """
                SELECT COUNT(*)
                FROM enrollment
                WHERE course_id = ? AND learner_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, courseId, userID);

        return count != null && count > 0;
    }

    private static final Logger logger = LoggerFactory.getLogger(JdbcEnrollmentRepository.class);

    public List<EnrollmentInfoMentorDto> findAllEnrollmentsForMentor(int mentorId,String userTimeZone) {


        String sql = """
                SELECT 
                   CONCAT(l.first_name, ' ',l.last_name) AS full_name,
                   e.amount,
                   e.enrollment_time,
                   c.name as course_name
                FROM
                    users l
                JOIN enrollment e ON l.id = e.learner_id 
                JOIN course c ON e.course_id = c.id
                WHERE c.mentor_id = ? AND c.status = ?
                """;

        List<EnrollmentInfoMentorDto> enrollmentInfoMentorDtos = jdbcTemplate.query(sql, (rs, rowNum) -> {
            EnrollmentInfoMentorDto enrollmentInfoMentorDto = new EnrollmentInfoMentorDto();
            enrollmentInfoMentorDto.setLearnerName(rs.getString("full_name"));
            enrollmentInfoMentorDto.setAmount(rs.getDouble("amount"));

            String utcTimeString = rs.getString("enrollment_time");

            logger.info("enrollment time before changing "+utcTimeString+" "+userTimeZone);


            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.n");

            LocalDateTime localDateTime = LocalDateTime.parse(utcTimeString, dateTimeFormatter);

            Instant utcInstant = localDateTime.toInstant(ZoneOffset.UTC);

            ZoneId userZone = ZoneId.of(userTimeZone);

            ZonedDateTime userDateTime = utcInstant.atZone(userZone);

            String formattedTime = userDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a z"));

            logger.info("enrollment time after changing "+formattedTime);

            enrollmentInfoMentorDto.setEnrolledTime(formattedTime);
            enrollmentInfoMentorDto.setCourseName(rs.getString("course_name"));
            return enrollmentInfoMentorDto;
        },mentorId,"ACTIVE");

        return enrollmentInfoMentorDtos;
    }

}
