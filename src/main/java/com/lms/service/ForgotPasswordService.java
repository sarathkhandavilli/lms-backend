package com.lms.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

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
import com.lms.dto.MailBody;
import com.lms.model.ForgotPassword;
import com.lms.model.User;
import com.lms.repository.ForgotPasswordRepository;
import com.lms.repository.UserRepository;

@Service
public class ForgotPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);



    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    public ResponseEntity<CommonApiResponse> verifyEmail(String email) {
        logger.info("Verify email process started for: {}", email);

        CommonApiResponse response;

        User user = userRepository.findUserByMail(email);

        if (user == null) {
            logger.warn("Email not found: {}", email);
            response = new CommonApiResponse(false, "email not found", user);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

        forgotPasswordRepository.deleteByUserId(user.getId());

        int otp = otpGenerator();


        long expirationTimeMillis = System.currentTimeMillis() + 2 * 60 * 1000;
        Date expirationTime = new Date(expirationTimeMillis);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedExpirationTime = sdf.format(expirationTime);

        MailBody mailBody = MailBody.builder()
                            .to(email)
                            .text(otp + " is the OTP for your Forgot Password request. " +
                            "This OTP will expire at " + formattedExpirationTime + ".")
                            .subject("OTP for Forgot Password request")
                            .build();


        ForgotPassword fp = ForgotPassword.builder()
                                      .otp(otp)
                                      .expirationTime(expirationTime)
                                      .userId(user.getId())
                                      .build();

        logger.info("printing fp "+fp.getFpid()+" , "+fp.getOtp()+" , "+fp.getUserId());
        emailService.sendSimpleMessage(mailBody);

        

        forgotPasswordRepository.save(fp);
      

        logger.info("OTP sent to email: {} with OTP: {}", email, otp);
        response = new CommonApiResponse(true, "email sent for verification", fp);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public ResponseEntity<CommonApiResponse> verifyOtp(int otp, String email) {
        logger.info("Verifying OTP: {} for email: {}", otp, email);

        CommonApiResponse response;

        User user = userRepository.findUserByMail(email);

        if (user == null) {
            logger.warn("Email not found: {}", email);
            response = new CommonApiResponse(false, "email not found", user);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user.getId())
                .orElseThrow(() -> {
                    logger.error("Invalid OTP for email: {}", email);
                    return new RuntimeException("Invalid OTP for email" + email);
                });

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            logger.warn("OTP expired for email: {}", email);
            response = new CommonApiResponse(false, "Otp has expired", null);
            return new ResponseEntity<>(response,HttpStatus.EXPECTATION_FAILED);
        }

        logger.info("OTP verified successfully for email: {}", email);
        response = new CommonApiResponse(true, "OTP verified!", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


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
