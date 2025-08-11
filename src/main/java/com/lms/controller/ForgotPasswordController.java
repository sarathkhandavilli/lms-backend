package com.lms.controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.lms.dto.ChangePassword;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.MailBody;
import com.lms.model.ForgotPassword;
import com.lms.model.User;
import com.lms.repository.ForgotPasswordRepository;
import com.lms.repository.UserRepository;
import com.lms.service.EmailService;
import com.lms.service.ForgotPasswordService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;
    
    //send mail for email verification
    @PostMapping("/verifyMail")
    public ResponseEntity<CommonApiResponse> verifyEmail(@RequestParam("email") String email) {

        return forgotPasswordService.verifyEmail(email);
    }

    //verifying otp
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<CommonApiResponse> verifyOtp(@PathVariable int otp, @PathVariable String email) {
        
        return forgotPasswordService.verifyOtp(otp, email);

    }

    //changing password
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<CommonApiResponse> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email) {

        return forgotPasswordService.changePasswordHandler(changePassword, email);
    }
}
