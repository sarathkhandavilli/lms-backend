package com.lms.service;

import com.lms.dto.CategoryDto;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.UpdateCategoryDto;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.Category;
import com.lms.repository.CategoryRepository;
import com.lms.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<CommonApiResponse> addCategory(CategoryDto categoryDto) {
        CommonApiResponse response;

        logger.info("Attempting to add category with name: {}", categoryDto.getName());

        if (categoryDto.getName() == null || categoryDto.getDescription() == null) {
            response = new CommonApiResponse(false, "Name or description cannot be null", null);
            logger.warn("Category creation failed. Name or description was null.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Category category = CategoryDto.toEntity(categoryDto);
            category.setStatus("ACTIVE");
            Category created = categoryRepository.add(category);

            response = new CommonApiResponse(true, "Category created successfully", CategoryDto.toDto(created));
            logger.info("Category with name '{}' created successfully", categoryDto.getName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error creating category: {}", e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> updateCategory(UpdateCategoryDto updatecategoryDto) {
        CommonApiResponse response;

        logger.info("Attempting to update category with ID: {}", updatecategoryDto.getId());

        if (updatecategoryDto.getId() == 0) {
            response = new CommonApiResponse(false, "category id should not be null", null);
            logger.warn("Category ID was null or zero in update request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Category category = categoryRepository.findById(updatecategoryDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            category.setName(updatecategoryDto.getName());
            category.setDescription(updatecategoryDto.getDescription());

            Category updatedCategory = categoryRepository.add(category);
            UpdateCategoryDto updatedDto = UpdateCategoryDto.toDto(updatedCategory);

            response = new CommonApiResponse(true, "Category updated successfully", updatedDto);
            logger.info("Category with ID '{}' updated successfully", updatecategoryDto.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(ResourceNotFoundException e) {
            logger.error("Category with ID '{}' not found: {}", updatecategoryDto.getId(), e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error updating category with ID '{}': {}", updatecategoryDto.getId(), e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> deleteCategory(int categoryId) {
        CommonApiResponse response;

        logger.info("Attempting to delete category with ID: {}", categoryId);

        if (categoryId == 0) {
            response = new CommonApiResponse(false, "Category id should not be null", categoryId);
            logger.warn("Category ID was null or zero in delete request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            boolean isCourseDeactivated = courseRepository.deactiveCourseByCategory(categoryId);

            if (!isCourseDeactivated) {
                response = new CommonApiResponse(false, "Courses deactivation failed", null);
                logger.error("Failed to deactivate courses for category with ID: {}", categoryId);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            boolean isCategoryDeleted = categoryRepository.deleteById(categoryId);

            if (isCategoryDeleted) {
                response = new CommonApiResponse(true, "Category deleted successfully", null);
                logger.info("Category with ID '{}' deleted successfully", categoryId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response = new CommonApiResponse(false, "Category not found", null);
                logger.warn("Category with ID '{}' not found for deletion", categoryId);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            logger.error("Category with ID '{}' not found: {}", categoryId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error deleting category with ID '{}': {}", categoryId, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommonApiResponse> getAllCategories(String status) {
        CommonApiResponse response;

        logger.info("Fetching categories with status: {}", status);

        status = status.toUpperCase();

        try {
            List<Category> categories = categoryRepository.findAll(status);

            if (categories.isEmpty()) {
                response = new CommonApiResponse(false, "Categories not found", null);
                logger.warn("No categories found with status: {}", status);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<CategoryDto> categoryDtos = categories.stream()
                    .map(CategoryDto::toDto)
                    .collect(Collectors.toList());

            response = new CommonApiResponse(true, "Categories fetched successfully", categoryDtos);
            logger.info("Successfully fetched {} categories with status '{}'", categoryDtos.size(), status);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error fetching categories with status '{}': {}", status, e.getMessage());
            response = new CommonApiResponse(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
