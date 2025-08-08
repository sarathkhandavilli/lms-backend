package com.lms.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.MentorDetail;

import lombok.Data;

@Data
public class MentorDetailDto {

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    private int age;

    private int experience;

    private String qualification;

    private String profession;

    private MultipartFile profilePic;

    private int mentorId;

    public static MentorDetail toEntity(MentorDetailDto mentorDetailDto) {

        MentorDetail entity = new MentorDetail();


        entity.setId(mentorDetailDto.getId());
        entity.setAge(mentorDetailDto.getAge());
        entity.setExperience(mentorDetailDto.getExperience());
        entity.setQualification(mentorDetailDto.getQualification());
        entity.setProfession(mentorDetailDto.getProfession());
        
        return entity;

    }

     public static MentorDetailDto toDto(MentorDetail entity) {

        MentorDetailDto dto = new MentorDetailDto();


        dto.setId(entity.getId());
        dto.setAge(entity.getAge());
        dto.setExperience(entity.getExperience());
        dto.setQualification(entity.getQualification());
        dto.setProfession(entity.getProfession());
        return dto;
    }

}
