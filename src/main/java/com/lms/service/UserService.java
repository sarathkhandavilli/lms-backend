package com.lms.service;

import com.lms.dto.CommonApiResponse;
import com.lms.dto.LoginDto;
import com.lms.dto.MentorDetailDto;
import com.lms.dto.UserDto;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.MentorDetail;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.MentorDetailRepository;
import com.lms.repository.StorageRepository;
import com.lms.repository.UserRepository;
import com.lms.util.JwtTokenUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    StorageRepository storageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MentorDetailRepository mentorDetailRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<CommonApiResponse> registerUser(UserDto userDto) {

        CommonApiResponse response;

        if (userDto.getEmailId() == null) {
            logger.warn("Email is missing during registration: {}", userDto);
            response = new CommonApiResponse(false, "Email is empty", userDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userDto.getRole() == null) {
            logger.warn("Role is missing during registration: {}", userDto);
            response = new CommonApiResponse(false, "Role is missing", userDto);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            logger.info("Checking if user already exists with email: {}", userDto.getEmailId());
            User existingUser = userRepository.findUserByMail(userDto.getEmailId());

            if (existingUser != null) {
                
                logger.warn("User with email {} already exists", userDto.getEmailId());
                response = new CommonApiResponse(false, "User with mail already exists", userDto);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            userDto.setStatus("ACTIVE");
            User user = UserDto.toEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            User registered = userRepository.add(user);
            if (registered == null) {
                logger.error("User registration failed for: {}", userDto.getEmailId());
                response = new CommonApiResponse(false, "Failed to register", userDto);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            logger.info("User registered successfully: {}", registered.getEmailId());
            response = new CommonApiResponse(true, "Registered Successfully", registered);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> addMentorDetail(MentorDetailDto mentorDetailDto) {
        CommonApiResponse response;

        try {
            logger.info("Adding mentor details for mentorId: {}", mentorDetailDto.getMentorId());

            User mentor = userRepository.findById(mentorDetailDto.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!mentor.getRole().equals("MENTOR")) {
                logger.warn("User with id {} is not a mentor", mentor.getId());
                response = new CommonApiResponse(false, "Mentor not found", mentor);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            MentorDetail mentorDetail = mentorDetailDto.toEntity(mentorDetailDto);

            if (mentorDetailDto.getProfilePic() != null ) {
                String profilePic = storageRepository.store(mentorDetailDto.getProfilePic());
                mentorDetail.setProfilePic(profilePic);
                logger.info("Profile picture stored successfully: {}", profilePic);
            }

            MentorDetail detail = mentorDetailRepository.addMentorDetail(mentorDetail);
            if (detail == null) {
                logger.error("Failed to add mentor detail for mentorId: {}", mentorDetailDto.getMentorId());
                response = new CommonApiResponse(false, "Failed to add Mentor detail", null);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            mentor.setMentorDetailId(detail.getId());
            userRepository.add(mentor);

            logger.info("Mentor detail added successfully for mentorId: {}", mentorDetailDto.getMentorId());
            response = new CommonApiResponse(true, "Mentor detail added successfully", detail);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error while adding mentor details: {}", e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> login(LoginDto loginDto) {
        CommonApiResponse response;

        try {
            logger.info("Attempting login for email: {}", loginDto.getEmail());

            User user = userRepository.findUserByMail(loginDto.getEmail());

            if (user == null) {
                logger.warn("User with email {} not registered", loginDto.getEmail());
                response = new CommonApiResponse(false, "User with this mail is not registered", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()) 
                && !loginDto.getPassword().equals(user.getPassword())) {
                logger.warn("Incorrect password for user: {}", loginDto.getEmail());
                return new ResponseEntity<>(new CommonApiResponse(false, "Incorrect password", null), HttpStatus.UNAUTHORIZED);
            }


            if (!user.getStatus().equalsIgnoreCase("ACTIVE")) {
                logger.warn("User is inactive: {}", user.getEmailId());
                response = new CommonApiResponse(false, "User is inactive", user);
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

             
            boolean isPlain = loginDto.getPassword().equals(user.getPassword());

            if (!isPlain) {
                // Only authenticate if it's a hashed password
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                );
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
            String token = jwtTokenUtil.generateToken(userDetails);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("role", user.getRole());
            responseData.put("userId", user.getId());
            responseData.put("firstName",user.getFirstName());
            responseData.put("lastName",user.getLastName());
            

            logger.info("Login successful for user: {}", loginDto.getEmail());
            response = new CommonApiResponse(true, "Login successful", responseData);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", loginDto.getEmail(), e.getMessage());
            response = new CommonApiResponse(true, e.getMessage(), "nothing");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> getUsersByRole(String role) {
        CommonApiResponse response;

         
        if (role == null || role.isEmpty()) {
            logger.warn("Role is missing in request.");
            response = new CommonApiResponse(false, "Role is missing", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {

            List<User> users = userRepository.findUsersByRoleAndStatus(role.toUpperCase());

            if (users.isEmpty()) {
                logger.info("No users found with role: {}", role);
                response = new CommonApiResponse(false, "No users found", users);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<UserDto> userDtos = new ArrayList<>();
            if ("MENTOR".equalsIgnoreCase(role)) {
                for (User user : users) {
                    user.setMentorDetail(mentorDetailRepository.findById(user.getMentorDetailId()));
                    userDtos.add(UserDto.toDto(user));
                }
            } else {
                for (User user : users) {
                    userDtos.add(UserDto.toDto(user));
                }
            }

            // for (User user : users) {
            //     userDtos.add(UserDto.toDto(user));
            // }

            logger.info("Users fetched successfully for role: {}", role);
            response = new CommonApiResponse(true, "Users Fetched Successfully", userDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error while fetching users by role: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> getMentorById(int mentorId) {
        CommonApiResponse response;

         
        if (mentorId == 0) {
            logger.warn("Missing mentorId in request.");
            response = new CommonApiResponse(false, "Missing input", mentorId);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            User mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

            if (!"MENTOR".equalsIgnoreCase(mentor.getRole())) {
                logger.warn("User with id {} is not a mentor.", mentorId);
                response = new CommonApiResponse(false, "Mentor not found", null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            MentorDetail mentorDetail = mentorDetailRepository.findById(mentor.getMentorDetailId());

            if (mentorDetail == null) {
                response = new CommonApiResponse(false, "Mentor Details not found", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            UserDto userDto = UserDto.toDto(mentor);
            userDto.setMentorDetail(mentorDetail);

            logger.info("Fetched mentor details for mentorId: {}", mentorId);
            response = new CommonApiResponse(true, "Fetched Mentor Profile", userDto);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            logger.error("Mentor not found for mentorId: {}", mentorId);
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error while fetching mentor details: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void fetchMentorImage(String mentorImageName, HttpServletResponse resp) {
        try {
            Resource resource = storageRepository.load(mentorImageName);

            if (resource != null) {
                try (InputStream in = resource.getInputStream(); 
                     ServletOutputStream out = resp.getOutputStream()) {
                    FileCopyUtils.copy(in, out);
                }
                logger.info("Mentor image sent successfully: {}", mentorImageName);
            } else {
                logger.warn("Mentor image not found: {}", mentorImageName);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (IOException e) {
            logger.error("Error while fetching mentor image: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> deleteMentor(int mentorId) {
        CommonApiResponse response;

         
        if (mentorId == 0) {
            logger.warn("Missing mentorId in request for deletion.");
            response = new CommonApiResponse(false, "Missing data", mentorId);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            User mentor = userRepository.findById(mentorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

             
            boolean isCourseDeactivated = courseRepository.deactiveCourseByMentor(mentorId);

            if (!isCourseDeactivated) {
                logger.error("Failed to deactivate courses for mentorId: {}", mentorId);
                response = new CommonApiResponse(false, "Courses Deactivation Failed", null);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            boolean isMentorDeleted = userRepository.deleteById(mentorId);

            if (isMentorDeleted) {
                logger.info("Mentor deleted successfully: {}", mentorId);
                response = new CommonApiResponse(true, "Mentor deleted successfully", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.warn("Mentor not found for deletion: {}", mentorId);
                response = new CommonApiResponse(false, "Mentor not found", null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.error("Error while deleting mentor with id {}: {}", mentorId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
