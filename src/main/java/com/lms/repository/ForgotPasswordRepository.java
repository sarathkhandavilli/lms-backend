package com.lms.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lms.model.ForgotPassword;
import com.lms.model.User;

@Repository
public interface ForgotPasswordRepository {
    
    void save(ForgotPassword fp);

    void deleteById(int fpId);
    
    void deleteByUserId(int uId);

    Optional<ForgotPassword> findByOtpAndUser(int otp, int userId);
}
