package com.lms.repository.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lms.model.ForgotPassword;
import com.lms.repository.ForgotPasswordRepository;

@Repository
public class JdbcForgotPasswordRepository implements ForgotPasswordRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(ForgotPassword fp) {

        String sql = "INSERT INTO forgot_password ( otp, expiration_time, user_id) VALUES( ?, ?, ?)";
        jdbcTemplate.update(sql, fp.getOtp(), fp.getExpirationTime(), fp.getUserId());
    }

    @Override
    public Optional<ForgotPassword> findByOtpAndUser(int otp, int userId) {
        
        String sql = "SELECT * FROM forgot_password WHERE otp = ? AND user_id = ?";

            try {
                ForgotPassword fp = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                    ForgotPassword forgotPassword = new ForgotPassword();
                    forgotPassword.setFpid(rs.getInt("fpid"));
                    forgotPassword.setOtp(rs.getInt("otp"));
                    forgotPassword.setExpirationTime(rs.getTimestamp("expiration_time"));
                    forgotPassword.setUserId(rs.getInt("user_id"));
                    return forgotPassword;
                }, otp, userId);

                return Optional.of(fp);

            } catch (org.springframework.dao.EmptyResultDataAccessException e) {
                return Optional.empty();
            }
        
    }

    @Override
    public void deleteById(int fpId) {

        String sql = "DELETE FROM forgot_password WHERE fpid = ?";
        jdbcTemplate.update(sql,fpId);
    }

    @Override
    public void deleteByUserId(int uId) {

        String sql = "DELETE FROM forgot_password WHERE user_id = ?";

        jdbcTemplate.update(sql,uId);

    }
}
