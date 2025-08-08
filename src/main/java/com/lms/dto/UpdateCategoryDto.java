package com.lms.dto;

import com.lms.model.Category;

import lombok.Data;

@Data
public class UpdateCategoryDto {

    private int id;

    private String name;

    private String description;

    private String status;

    public static UpdateCategoryDto toDto(Category category) {

        UpdateCategoryDto categoryDto = new UpdateCategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setStatus(category.getStatus());

        return categoryDto;
    }

    public static Category toEntity(UpdateCategoryDto updatecategoryDto) {

        Category category = new Category();

        category.setId(updatecategoryDto.getId());
        category.setName(updatecategoryDto.getName());
        category.setDescription(updatecategoryDto.getDescription());
        category.setStatus(updatecategoryDto.getStatus());

        return category;
    }
}
