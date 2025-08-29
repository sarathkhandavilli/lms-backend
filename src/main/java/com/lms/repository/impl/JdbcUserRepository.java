package com.lms.repository.impl;

import com.lms.model.RegistrationOtp;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?"; 

        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmailId(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setPhoneNo(rs.getString("phone_no"));
                u.setRole(rs.getString("role").toUpperCase());
                u.setStatus(rs.getString("status"));
                u.setMentorDetailId(rs.getInt("mentor_detail_id"));
                u.setAmount(rs.getDouble("amount"));
                u.setMentorDetailId(rs.getInt("mentor_detail_id"));
                return u;
            }, id);

            return Optional.of(user); 
        } catch (Exception e) {
            return Optional.empty(); 
        }
    }

    @Override
    public User add(User user) {

        if (user.getMentorDetailId() ==  0 ) {

            String sql = "INSERT INTO users (first_name, last_name, email, password, phone_no, role, status) VALUES(?,?,?,?,?,?,?) RETURNING id";

            Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                user.getFirstName(),
                user.getLastName(),
                user.getEmailId(),
                user.getPassword(),
                user.getPhoneNo(),
                user.getRole(),
                user.getStatus()
            );

            user.setId(generatedId);

        } else {

            String sql = "UPDATE users SET mentor_detail_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                user.getMentorDetailId(),
                user.getId()
            );
        }

        return user;

    }

    @Override
    public User findUserByMail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmailId(rs.getString("email")); // make sure column name matches
                user.setPassword(rs.getString("password"));
                user.setPhoneNo(rs.getString("phone_no"));
                user.setRole(rs.getString("role").toUpperCase());
                user.setStatus(rs.getString("status"));
                user.setAmount(rs.getDouble("amount"));
                user.setMentorDetailId(rs.getInt("mentor_detail_id"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No user found with email: " + email);
            return null;
        } catch (Exception e) {
            e.printStackTrace(); // For debugging: maybe a column mismatch
            return null;
        }
    }


    @Override
    public boolean deleteById(int mentorId) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,"INACTIVE",mentorId);

        return rowsAffected > 0;
    }

    public List<User> findUsersByRoleAndStatus(String role) {

        role = role.equals("MENTOR") ? role.toUpperCase() : role;
        String sql = "SELECT * FROM users u WHERE role = ? AND status = ? ORDER BY u.id DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmailId(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNo(rs.getString("phone_no"));
        user.setRole(rs.getString("role").toUpperCase());
        user.setStatus(rs.getString("status"));
        user.setMentorDetailId(rs.getInt("mentor_detail_id"));
        user.setAmount(rs.getDouble("amount"));
        return user;
    },role,"ACTIVE");
    }

    public User update(User mentor) {
        String sql = "UPDATE users SET amount = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(
        sql,
        mentor.getAmount(),
        mentor.getId()
    );

    if (rowsAffected == 0) {
        throw new RuntimeException("Failed to update mentor's amount. User not found with id: " + mentor.getId());
    }

    return mentor;

    }

    @Override
    public void updatePassword(String email, String password) {

        String sql = "UPDATE users SET password = ? WHERE email = ?";

        jdbcTemplate.update(sql,password,email);
        
    }

    @Override
    public RegistrationOtp checkOtp(int otp, String email) {
        String sql = "SELECT * FROM registration_otps WHERE otp = ? AND email = ?";

        try {
            RegistrationOtp registrationOtp = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                RegistrationOtp ro = new RegistrationOtp();
                ro.setId(rs.getInt("id"));
                ro.setOtp(rs.getInt("otp"));
                ro.setExpirationTime(rs.getTimestamp("expiration_time"));
                ro.setEmail(rs.getString("email"));
                return ro;
            }, otp, email);

            return registrationOtp;

        } catch (Exception e) {
            return null ;
        }
    }

    @Override
    public void deleteOtp(int id) {

        String sql = "DELETE FROM registration_otps WHERE id = ?";
        jdbcTemplate.update(sql,id);

    }

    @Override
    public void saveRegistrationOtp(RegistrationOtp registrationOtp) {
        String sql = "INSERT INTO registration_otps (otp, email, expiration_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,registrationOtp.getOtp(),registrationOtp.getEmail(),registrationOtp.getExpirationTime());
    }

    @Override
    public void deletePrevOtpsByMail(String email) {

        String sql = "DELETE FROM registration_otps WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }
}
