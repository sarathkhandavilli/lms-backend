package com.lms.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lms.model.MentorDetail;
import com.lms.repository.MentorDetailRepository;

@Repository
public class JdbcMentorDetailRepository implements MentorDetailRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public MentorDetail addMentorDetail(MentorDetail mentorDetail) {

        String sql = "INSERT INTO mentor_detail (age, experience, qualification, profession, profile_pic) VALUES(?,?,?,?,?) RETURNING id";

        Integer generatedId = jdbcTemplate.queryForObject(sql,Integer.class,
            mentorDetail.getAge(),
            mentorDetail.getExperience(),
            mentorDetail.getQualification(),
            mentorDetail.getProfession(),
            mentorDetail.getProfilePic()
        );

        mentorDetail.setId(generatedId);
        return mentorDetail;
    }

    @Override
    public MentorDetail findById(int id) {
        String sql = "SELECT * FROM mentor_detail WHERE id = ?";

        List<MentorDetail> list = jdbcTemplate.query(sql,(rs, rowNum) -> {
            MentorDetail mentorDetail = new MentorDetail();
            mentorDetail.setId(rs.getInt("id"));
            mentorDetail.setAge(rs.getInt("age"));
            mentorDetail.setExperience(rs.getInt("experience"));
            mentorDetail.setQualification(rs.getString("qualification"));
            mentorDetail.setProfession(rs.getString("profession"));
            mentorDetail.setProfilePic(rs.getString("profile_pic"));
            return mentorDetail;
        },id);

        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
