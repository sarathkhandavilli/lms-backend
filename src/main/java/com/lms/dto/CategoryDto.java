package com.lms.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lms.model.Category;
import lombok.Data;

@Data
public class CategoryDto implements Serializable{

    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private int id;

    private String name;

    private String description;

    private String status;

    public static CategoryDto toDto(Category category) {

        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setStatus(category.getStatus());

        return categoryDto;
    }

    public static Category toEntity(CategoryDto categoryDto) {

        Category category = new Category();

        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setStatus(categoryDto.getStatus());

        return category;
    }
}
