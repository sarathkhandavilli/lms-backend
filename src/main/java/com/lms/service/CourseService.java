package com.lms.service;

import com.lms.dto.*;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.*;
import com.lms.repository.*;
// import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private CategoryRepository categoryRepository;

    @Autowired 
    private CourseRepository courseRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired 
    private CourseSectionTopicRepository courseSectionTopicRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Caching( evict = {
        @CacheEvict(value = "courses", allEntries = true),
        @CacheEvict(value = "mentorDashboard", key = "#courseDto.mentorId" )
    })
    public CourseDto addCourse(CourseDto courseDto) throws IOException {
        CommonApiResponse response;

        logger.info("Attempting to add course with name: {}", courseDto.getName());

            User mentor = userRepository.findById(courseDto.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!mentor.getRole().equals("MENTOR")) {
                logger.warn("User with ID {} is not a mentor", courseDto.getMentorId());
                throw new ResourceNotFoundException("Mentor not found");
            }

            Category category = categoryRepository.findById(courseDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            Course course = CourseDto.toEntity(courseDto);

            MultipartFile courseThumbnail = courseDto.getThumbnail();
            if (courseThumbnail != null) {
                String courseThumbnailName = storageRepository.store(courseDto.getThumbnail());
                course.setThumbnailName(courseThumbnailName);

                byte[] imageBytes = courseThumbnail.getBytes();
                Cache cache = cacheManager.getCache("courseImages");
                if (cache != null) {
                    cache.put(courseThumbnailName,imageBytes);
                }
                
                logger.info("Thumbnail for course '{}' uploaded successfully", courseDto.getName());
            }

            course.setMentorId(mentor.getId());
            course.setCategoryId(category.getId());
            course.setStatus("ACTIVE");

            CourseDto created = CourseDto.toDto(courseRepository.save(course));

            return created;
            
    }

    public ResponseEntity<CommonApiResponse> addCourseSection(CourseSectionDto dto) {
        CommonApiResponse response;

        logger.info("Attempting to add course section for course ID: {}", dto.getCourseId());

        try {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

            CourseSection section = CourseSectionDto.toEntity(dto);
            section.setCourseId(course.getId());

            CourseSectionDto created = CourseSectionDto.toDto(courseSectionRepository.add(section));
            response = new CommonApiResponse(true, "Course Section added successfully", created);
            logger.info("Course section added successfully for course ID: {}", dto.getCourseId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            logger.error("Error adding course section: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error adding course section: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> addCourseSectionTopic(CourseSectionTopicDto dto) {
        CommonApiResponse response;

        logger.info("Attempting to add course section topic for section ID: {}", dto.getSectionId());

        try {
            CourseSection section = courseSectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

            CourseSectionTopic topic = CourseSectionTopicDto.toEntity(dto);
            topic.setSectionId(section.getId());

            CourseSectionTopicDto created = CourseSectionTopicDto.toDto(courseSectionTopicRepository.add(topic));
            response = new CommonApiResponse(true, "Course Section Topic added successfully", created);
            logger.info("Course section topic added successfully for section ID: {}", dto.getSectionId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            logger.error("Error adding course section topic: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error adding course section topic: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<CommonApiResponse> fetchCourseById(int id) {
        CommonApiResponse response;

        logger.info("Fetching course details for course ID: {}", id);

        try {
            CourseDetailsDto courseDetailsDto = courseRepository.findCourseDetailsById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Course Details Not Found"));

            logger.info("Course details fetched successfully for course ID: {}", id);
            response = new CommonApiResponse(true, "Course Details Fetched Successfully", courseDetailsDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching course details for course ID {}: {}", id, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching course details for course ID {}: {}", id, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> fetchCourseByIdAndUserId(int courseId, int userId) {
        CommonApiResponse response;

        logger.info("Fetching course details for course ID: {} and user ID: {}", courseId, userId);

        try {

         
            CourseDetailsDto courseDetailsDto = courseRepository.findCourseDetailsByCourseIdAndUserId(courseId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course Details Not Found"));

            logger.info("Course details fetched successfully for course ID: {} and user ID: {}", courseId, userId);
            response = new CommonApiResponse(true, "Entire Course Details Fetched Successfully", courseDetailsDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching course details for course ID {} and user ID {}: {}", courseId, userId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching course details for course ID {} and user ID {}: {}", courseId, userId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "courses", key = "#status")
    public List<CourseDto> fetchCourseByStatus(String status) {

        List<Course> courses = courseRepository.findByStatus(status.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Course with status " + status + " not found"));

        return courses.stream()
                .map(CourseDto::toDto)
                .collect(Collectors.toList());
    }

    


    public ResponseEntity<CommonApiResponse> fetchCourseByMentor(int mentorId, String status) {
        CommonApiResponse response;

        logger.info("Fetching courses for mentor ID: {} with status: {}", mentorId, status);

        try {
            User mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!mentor.getRole().equals("MENTOR")) {
                logger.warn("User ID {} is not a mentor", mentorId);
                throw new ResourceNotFoundException("Mentor Not Found");
            }

            List<Course> courses = courseRepository.findByMentorAndStatus(mentor.getId(), status.toUpperCase());

            if (courses.isEmpty()) {
                logger.warn("No courses found for mentor ID: {} with status: {}", mentorId, status);
                response = new CommonApiResponse(false, "Course not found for the given mentor and status", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<CourseDto> courseDtos = courses.stream()
                    .map(CourseDto::toDto)
                    .collect(Collectors.toList());

            logger.info("Courses fetched successfully for mentor ID: {} with status: {}", mentorId, status);
            response = new CommonApiResponse(true, "Course fetched successfully", courseDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching courses for mentor ID {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching courses for mentor ID {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> fetchCourseByCategory(int categoryId, String status) {
        CommonApiResponse response;

        logger.info("Fetching courses for category ID: {} with status: {}", categoryId, status);

        try {
            List<Course> courses = courseRepository.findCourseByCategory(categoryId, status.toUpperCase());

            if (courses.isEmpty()) {
                logger.warn("No courses found for category ID: {} with status: {}", categoryId, status);
                throw new ResourceNotFoundException("No courses found");
            }

            List<CourseDto> courseDtos = courses.stream()
                    .map(CourseDto::toDto)
                    .collect(Collectors.toList());

            logger.info("Courses fetched successfully for category ID: {} with status: {}", categoryId, status);
            response = new CommonApiResponse(true, "Category fetched successfully", courseDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching courses for category ID {}: {}", categoryId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching courses for category ID {}: {}", categoryId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> fetchCourseByName(String courseName, String status) {
        CommonApiResponse response;

        logger.info("Fetching courses with name: {} and status: {}", courseName, status);

        try {
            List<Course> courses = courseRepository.findByNameAndStatus(courseName, status.toUpperCase())
                    .orElseThrow(() -> new ResourceNotFoundException("No Courses Found"));

            List<CourseDto> dtos = courses.stream()
                    .map(CourseDto::toDto)
                    .collect(Collectors.toList());

            logger.info("Courses fetched successfully for name: {} with status: {}", courseName, status);
            response = new CommonApiResponse(true, "Courses fetched successfully", dtos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            logger.error("Error fetching courses with name {} and status {}: {}", courseName, status, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching courses with name {} and status {}: {}", courseName, status, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "mentorDashboard", key = "#mentorId" )
    public boolean deleteCourse(int courseId,int mentorId) {

        CommonApiResponse response;

        logger.info("Attempting to delete course with ID: {}", courseId);

        boolean isDeleted = courseRepository.deleteById(courseId,mentorId);

        if (isDeleted) {
            logger.info("Course with ID: {} deleted successfully", courseId);
            return true;
        } else {
            logger.warn("Course with ID: {} not found for deletion", courseId);
            return false ;
        }
    }

    public ResponseEntity<CommonApiResponse> fetchYoutubeUrl(int courseSectionTopicId) {

        CommonApiResponse response;

        logger.info("Fetching YouTube URL for course section topic ID: {}", courseSectionTopicId);

        try {
            String youtubeUrl = courseSectionTopicRepository.findYoutubeUrl(courseSectionTopicId);

            if (youtubeUrl == null) {
                logger.warn("No YouTube URL found for course section topic ID: {}", courseSectionTopicId);
                response = new CommonApiResponse(false, "Youtube URL not found", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            logger.info("YouTube URL fetched successfully for course section topic ID: {}", courseSectionTopicId);
            response = new CommonApiResponse(true, "Youtube URL fetched successfully", youtubeUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Error occurred while fetching YouTube URL for course section topic ID: {}: {}", courseSectionTopicId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "mentorDashboard", key = "#mentorId")
    public MentorDashBoardDto fetchMentorDashboardData(int mentorId) {

        CommonApiResponse response;

        logger.info("From DB: Fetching mentor dashboard data for mentor ID: {}", mentorId);

 

            User mentor = userRepository.findById(mentorId).orElseThrow( () -> new ResourceNotFoundException("User Not Found"));

            if (!mentor.getRole().equals("MENTOR")) {
                logger.warn("User with ID {} is not a mentor", mentorId);
                throw new ResourceNotFoundException("Mentor Not Found");
            }

            MentorDashBoardDto dto = new MentorDashBoardDto();

            dto.setTotalActiveCourses(courseRepository.findCountByMentorAndStatus(mentorId, "ACTIVE"));
            dto.setTotalDeletedCourses(courseRepository.findCountByMentorAndStatus(mentorId, "INACTIVE"));
            dto.setTotalCoursePurchases(courseRepository.findCountPurchasedByMentor(mentorId));
            dto.setTotalSale(mentor.getAmount());

            return dto;
            
    }

    @Cacheable(value = "courseImages", key = "#thumbnailName")
    public byte[] fetchCourseImage(String thumbnailName, HttpServletResponse resp) {

        logger.info("FROM DB : Attempting to fetch course image for thumbnail: {} ", thumbnailName);

        Resource resource = storageRepository.load(thumbnailName);

        if (resource != null) {
            try (InputStream in = resource.getInputStream()) {
                 byte[] imageBytes = in.readAllBytes();
                logger.info("Course image for thumbnail {} cached successfully", thumbnailName);
                return imageBytes;
            } catch (IOException e) {
                logger.error("Error while fetching course image for thumbnail {}: {}", thumbnailName, e.getMessage());
                return null;
            }
        } else {
            logger.warn("No course image found for thumbnail: {}", thumbnailName);
            return null;
        }
    }
}
