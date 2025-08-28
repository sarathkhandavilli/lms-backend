package com.lms.controller;

import com.lms.constants.OtpType;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.LoginDto;
import com.lms.dto.MentorDetailDto;
import com.lms.dto.UserDto;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.service.EmailService;
import com.lms.service.OtpService;
import com.lms.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OtpService otpService;
    
    @PostMapping("/verifymail")
    public ResponseEntity<CommonApiResponse> verifymail(@RequestParam("email") String email) {
        return otpService.sendOtp(email, OtpType.REGISTRATION);
    }

    @PostMapping("/verifyotp/{otp}/{email}")
    public ResponseEntity<CommonApiResponse> verifyotp(
            @PathVariable int otp,
            @PathVariable String email) {
        return otpService.verifyOtp(otp, email, OtpType.REGISTRATION);
    }


    // registers the user (any one can)
    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse> register(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }


    //adds mentor details (mentor can)
    @PostMapping(value = "/mentordetail/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonApiResponse> addMentorDetail(@ModelAttribute MentorDetailDto mentorDetailDto) {
        return userService.addMentorDetail(mentorDetailDto);
    }

    // log's the user 
    @PostMapping("/login")
    public ResponseEntity<CommonApiResponse> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    // fetches the particular type of users(mentors,learners) for admin to view all the mentors,learners
    @GetMapping("/fetch/role-wise")
    public ResponseEntity<CommonApiResponse> getUsersByRole(@RequestParam("role") String role) {

        CommonApiResponse response;

        List<UserDto> userDtos = userService.getUsersByRole(role);

        if (userDtos == null) {
            response = new CommonApiResponse(false, "BAD_REQUEST OR INTERNAL_SERVER_ERROR", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(userDtos.isEmpty()) {
            response = new CommonApiResponse(false, "No users found", userDtos);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            response = new CommonApiResponse(true, "Users Fetched Successfully", userDtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // fetches particular mentor profile
    @GetMapping("/fetch/mentor-id")
    public ResponseEntity<CommonApiResponse> getMentor(@RequestParam("mentorId") int mentorId) {
        CommonApiResponse response;

        UserDto userDto = userService.getMentorById(mentorId);

        if (userDto == null) {
            response = new CommonApiResponse(false, "Missing input or mentor not found or mentor details not found or INTERNAL_SERVER_ERROR", mentorId);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response = new CommonApiResponse(true, "Fetched Mentor Profile", userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // deactivates mentor (admin can)
    @DeleteMapping("/mentor/delete")
    public ResponseEntity<CommonApiResponse> deleteMentor(@RequestParam("mentorId") int mentorId, @RequestParam("mentorImageName") String mentorImageName) {
        return userService.deleteMentor(mentorId,mentorImageName);
    }

    // fetches mentor profile pic to display
    @GetMapping(value="/fetch/{mentorImageName}", produces="image/*")
    public void getMentorImage(@PathVariable("mentorImageName") String mentorImageName, HttpServletResponse resp) throws IOException {
       byte[] imageBytes = userService.fetchMentorImage(mentorImageName, resp);

       if (imageBytes != null) {
        resp.getOutputStream().write(imageBytes);
       } else {
        resp.setStatus(HttpStatus.NOT_FOUND.value());
       }
    }

}
