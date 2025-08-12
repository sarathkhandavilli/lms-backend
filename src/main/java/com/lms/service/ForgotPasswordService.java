package com.lms.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.controller.ForgotPasswordController;
import com.lms.dto.ChangePassword;
import com.lms.dto.CommonApiResponse;
import com.lms.repository.UserRepository;

@Service
public class ForgotPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);


    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<CommonApiResponse> changePasswordHandler(ChangePassword changePassword, String email) {
        logger.info("Password change request for email: {}", email);

        CommonApiResponse response;

        if (!changePassword.password().equals(changePassword.repeatPassword())) {
            System.out.println(changePassword.password()+" --- "+changePassword.repeatPassword());
            logger.warn("Password mismatch for email: {}", email);
            response = new CommonApiResponse(false, "Both passwords doesn't match, please enter again!", null);
            return new ResponseEntity<>(response,HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());

        userRepository.updatePassword(email, encodedPassword);

        logger.info("Password changed successfully for email: {}", email);
        response = new CommonApiResponse(true, "Password has been changed successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
