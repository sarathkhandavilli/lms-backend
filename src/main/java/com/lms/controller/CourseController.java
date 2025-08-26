package com.lms.controller;

import com.lms.dto.*;
import com.lms.exception.ResourceNotFoundException;
import com.lms.service.CourseService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    // adds courses (mentor can add)
    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<CommonApiResponse> addCourse(@ModelAttribute CourseDto courseDto) {
        CommonApiResponse response;
        

        try {

            CourseDto created = courseService.addCourse(courseDto);
        response = new CommonApiResponse(true, "Course created successfully", created);
            logger.info("Course with name '{}' created successfully", courseDto.getName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            logger.error("Error creating course: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error creating course: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // adds section (mentor can add)
    @PostMapping("/section/add")
    public ResponseEntity<CommonApiResponse> addCourseSection(@RequestBody CourseSectionDto courseSectionDto) {
        return courseService.addCourseSection(courseSectionDto);
    }

    // adds topics (mentor can add)
    @PostMapping("section/topic/add")
    public ResponseEntity<CommonApiResponse> addCourseSectionTopic(@RequestBody CourseSectionTopicDto courseSectionTopicDto) {
        return courseService.addCourseSectionTopic(courseSectionTopicDto);
    }

    // fetches required course details when no one is logged in (any one)
    @GetMapping("fetch/course-id")
    public ResponseEntity<CommonApiResponse> fetchCourseById(@RequestParam("courseId") int courseId) {
        return courseService.fetchCourseById(courseId);
    }

    // fetches the entire course details when user logged in (particular user)
    @GetMapping("fetch/course-user-id")
    public ResponseEntity<CommonApiResponse> fetchCourseByIdAndUserId(@RequestParam("cid") int courseId, @RequestParam("uid") int userId) {
        return courseService.fetchCourseByIdAndUserId(courseId, userId);
    }

    // fetches all the active courses in the webpage(home page) (any one)
    @GetMapping("fetch/status-wise")
    public ResponseEntity<CommonApiResponse> fetchCourseByStatus(@RequestParam("status") String status) {
        List<CourseDto> courseDtos = courseService.fetchCourseByStatus(status);
        CommonApiResponse response = new CommonApiResponse(true, "Course fetched successfully", courseDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // fetches all the courses that a mentor created (to see in my courses of mentor)
    @GetMapping("fetch/mentor-wise")
    public ResponseEntity<CommonApiResponse> fetchCourseByMentor(@RequestParam("mentorId") int mentorId, @RequestParam("status") String status) {
        return courseService.fetchCourseByMentor(mentorId, status);
    }

    // filters courses according to category
    @GetMapping("fetch/category-wise")
    public ResponseEntity<CommonApiResponse> fetchCourseByCategory(@RequestParam("categoryId") int categoryId, @RequestParam("status") String status) {
        return courseService.fetchCourseByCategory(categoryId,status);
    }

    // filters courses according to name
    @GetMapping("fetch/name-wise")
    public ResponseEntity<CommonApiResponse> fetchCourseByName(@RequestParam("courseName") String courseName, @RequestParam("status") String status) {
        return courseService.fetchCourseByName(courseName,status);
    }

    // deactives the course
    @DeleteMapping("/delete")
    public ResponseEntity<CommonApiResponse> deleteCourse(@RequestParam("courseId") int courseId, @RequestParam("mentorId") int mentorId) {
        CommonApiResponse response;
        boolean isDeleted =  courseService.deleteCourse(courseId,mentorId);
        if(isDeleted) {
            response = new CommonApiResponse(isDeleted, "Course Deleted Successfully", null);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } else {
            response = new CommonApiResponse(false, "Course not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // fetches the youtube url for particular topic
    @GetMapping("/youtube/{courseSectionTopicId}")
    public ResponseEntity<CommonApiResponse> fetchYoutubeUrl(@PathVariable int courseSectionTopicId) {
        return courseService.fetchYoutubeUrl(courseSectionTopicId);
    }

    // fetches the mentor dashboard which includes (total courses active,inactive , total courses purchased , total earnings of mentor, )
    @GetMapping("mentor/dashboard")
    public ResponseEntity<CommonApiResponse> fetchMentorDashBoardData(@RequestParam("mentorId") int mentorId) {
        CommonApiResponse response;
        try {
        MentorDashBoardDto dto =  courseService.fetchMentorDashboardData(mentorId);
        logger.info("Mentor dashboard data fetched successfully for mentor ID: {}", mentorId);
            response = new CommonApiResponse(true, "Mentor dashboard data fetched successfully", dto);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching mentor dashboard data for mentor ID {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), mentorId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            logger.error("Unexpected error while fetching mentor dashboard data for mentor ID {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), mentorId);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // fetches course thumbnail to display
    @GetMapping("fetch/{thumbnailName}")
    public void fetchCourseImage(@PathVariable("thumbnailName") String thumbnailName, HttpServletResponse resp) throws IOException {
        byte[] imageBytes = courseService.fetchCourseImage(thumbnailName,resp);

        if (imageBytes != null) {
            resp.getOutputStream().write(imageBytes);
        } else {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
    