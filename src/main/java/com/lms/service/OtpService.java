package com.lms.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lms.constants.OtpType;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.MailBody;
import com.lms.model.ForgotPassword;
import com.lms.model.RegistrationOtp;
import com.lms.model.User;
import com.lms.repository.ForgotPasswordRepository;
import com.lms.repository.UserRepository;

@Service
public class OtpService {

     @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public ResponseEntity<CommonApiResponse> sendOtp(String email, OtpType type) {
        CommonApiResponse response;

        if (type == OtpType.FORGOT_PASSWORD) {
            User user = userRepository.findUserByMail(email);
            if (user == null) {
                return new ResponseEntity<>(
                    new CommonApiResponse(false, "Email not found", null),
                    HttpStatus.NOT_FOUND
                );
            }
            forgotPasswordRepository.deleteByUserId(user.getId());
        }

        int otp = otpGenerator();
        Date expirationTime = new Date(System.currentTimeMillis() + 3 * 60 * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set your desired time zone

        String formattedTime = sdf.format(expirationTime);

        MailBody mailBody = MailBody.builder()
            .to(email)
            .text(otp + " is your OTP for " + type.name().replace("_", " ").toLowerCase() +
                  ". It will expire at " + formattedTime + ".")
            .subject("OTP for " + type.name().replace("_", " ").toLowerCase())
            .build();

        emailService.sendSimpleMessage(mailBody);

        if (type == OtpType.REGISTRATION) {
            userRepository.deletePrevOtpsByMail(email);
            RegistrationOtp ro = RegistrationOtp.builder()
                .otp(otp)
                .email(email)
                .expirationTime(expirationTime)
                .build();
            userRepository.saveRegistrationOtp(ro);
        } else {
            User user = userRepository.findUserByMail(email);
            ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .userId(user.getId())
                .expirationTime(expirationTime)
                .build();
            forgotPasswordRepository.save(fp);
        }

        response = new CommonApiResponse(true, "OTP sent successfully!", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CommonApiResponse> verifyOtp(int otp, String email, OtpType type) {
        CommonApiResponse response;

        if (type == OtpType.REGISTRATION) {
            RegistrationOtp ro = userRepository.checkOtp(otp, email);
            if (ro == null) {
                return new ResponseEntity<>(new CommonApiResponse(false, "Invalid OTP", null), HttpStatus.UNAUTHORIZED);
            }
            if (ro.getExpirationTime().before(new Date())) {
                userRepository.deleteOtp(ro.getId());
                return new ResponseEntity<>(new CommonApiResponse(false, "OTP expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            User user = userRepository.findUserByMail(email);
            ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user.getId())
                .orElse(null);
            if (fp == null) {
                return new ResponseEntity<>(new CommonApiResponse(false, "Invalid OTP", null), HttpStatus.UNAUTHORIZED);
            }
            if (fp.getExpirationTime().before(new Date())) {
                forgotPasswordRepository.deleteById(fp.getFpid());
                return new ResponseEntity<>(new CommonApiResponse(false, "OTP expired", null), HttpStatus.EXPECTATION_FAILED);
            }
        }

        return new ResponseEntity<>(new CommonApiResponse(true, "OTP verified!", null), HttpStatus.OK);
    }
    
}
