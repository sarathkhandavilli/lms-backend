package com.lms.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.MentorDetail;
import com.lms.model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto implements Serializable {

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Double amount;

    private String firstName;

    private String lastName;

    private String emailId;

    private String password;

    private String phoneNo;

    private String role;

    private String status;

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int mentorDetailId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private MentorDetail mentorDetail;

    public static String getFormattedPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            return String.format("%s-%s-%s", 
                    phoneNumber.substring(0, 3), 
                    phoneNumber.substring(3, 6), 
                    phoneNumber.substring(6, 10));
        }
        return phoneNumber;
    }

    public static String getUnformattedPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            return phoneNumber.replaceAll("[^\\d]", "");
        }
        return phoneNumber;
    }

    public static User toEntity(UserDto userDto) {

        User user = new User();

        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailId(userDto.getEmailId());
        user.setPassword(userDto.getPassword());
        String formatted = getFormattedPhoneNumber(userDto.getPhoneNo());
        user.setPhoneNo(formatted);
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setMentorDetailId(userDto.getMentorDetailId());
        user.setAmount(userDto.getAmount());
        user.setMentorDetailId(userDto.getMentorDetailId());

        return user;
    }

    public static UserDto toDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmailId(user.getEmailId());
        userDto.setPassword(user.getPassword());
        String unformatted = getUnformattedPhoneNumber(user.getPhoneNo());
        userDto.setPhoneNo(unformatted);
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        userDto.setMentorDetailId(user.getMentorDetailId());
        userDto.setMentorDetail(user.getMentorDetail());
        userDto.setAmount(user.getAmount());

        return userDto;
    }
}
