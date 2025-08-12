package com.lms.controller;

import com.lms.constants.OtpType;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.LoginDto;
import com.lms.dto.MentorDetailDto;
import com.lms.dto.UserDto;
import com.lms.repository.UserRepository;
import com.lms.service.EmailService;
import com.lms.service.OtpService;
import com.lms.service.UserService;

import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
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
        return userService.getUsersByRole(role);
    }

    // fetches particular mentor profile
    @GetMapping("/fetch/mentor-id")
    public ResponseEntity<CommonApiResponse> getMentor(@RequestParam("mentorId") int mentorId) {
        return userService.getMentorById(mentorId);
    }

    // deactivates mentor (admin can)
    @DeleteMapping("/mentor/delete")
    public ResponseEntity<CommonApiResponse> deleteMentor(@RequestParam("mentorId") int mentorId) {
        return userService.deleteMentor(mentorId);
    }

    // fetches mentor profile pic to display
    @GetMapping(value="/fetch/{mentorImageName}", produces="image/*")
    public void getMentorImage(@PathVariable("mentorImageName") String mentorImageName, HttpServletResponse resp) {
        userService.fetchMentorImage(mentorImageName, resp);
    }

}
