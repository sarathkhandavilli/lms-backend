package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.lms.constants.OtpType;
import com.lms.dto.ChangePassword;
import com.lms.dto.CommonApiResponse;
import com.lms.service.ForgotPasswordService;
import com.lms.service.OtpService;


@RestController
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private OtpService otpService;
    
    @PostMapping("/verifyMail")
    public ResponseEntity<CommonApiResponse> verifyEmail(@RequestParam("email") String email) {
        return otpService.sendOtp(email, OtpType.FORGOT_PASSWORD);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<CommonApiResponse> verifyOtp(
            @PathVariable int otp,
            @PathVariable String email) {
        return otpService.verifyOtp(otp, email, OtpType.FORGOT_PASSWORD);
    }

    //changing password
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<CommonApiResponse> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email) {

        return forgotPasswordService.changePasswordHandler(changePassword, email);
    }
}
