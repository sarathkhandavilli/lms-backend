package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.CourseSectionTopic;
import lombok.Data;

@Data
public class CourseSectionTopicDto {

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    private int sectionId;

    private String topicNo;

    private String name;

    private String description;

    private String youtubeUrl;


    public static CourseSectionTopic toEntity(CourseSectionTopicDto dto) {

        CourseSectionTopic courseSectionTopic = new CourseSectionTopic();

        courseSectionTopic.setId(dto.getId());
        courseSectionTopic.setTopicNo(dto.getTopicNo());
        courseSectionTopic.setName(dto.getName());
        courseSectionTopic.setDescription(dto.getDescription());
        courseSectionTopic.setYoutubeUrl(dto.getYoutubeUrl());

        return courseSectionTopic;
    }

    public static CourseSectionTopicDto toDto(CourseSectionTopic courseSectionTopic) {

        CourseSectionTopicDto dto = new CourseSectionTopicDto();

        dto.setId(courseSectionTopic.getId());
        dto.setTopicNo(courseSectionTopic.getTopicNo());
        dto.setName(courseSectionTopic.getName());
        dto.setDescription(courseSectionTopic.getDescription());
        dto.setYoutubeUrl(courseSectionTopic.getYoutubeUrl());
        dto.setSectionId(courseSectionTopic.getSectionId());

        return dto;
    }


}
