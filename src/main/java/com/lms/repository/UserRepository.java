package com.lms.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.lms.model.User;

import java.util.Optional;

@Repository
public interface UserRepository {

    Optional<User> findById(int id);

    User add(User user);
    User findUserByMail(String email);
    List<User> findUsersByRoleAndStatus(String role);
    boolean deleteById(int mentorId);
    User update(User mentor);

    void updatePassword(String email, String password);

}
