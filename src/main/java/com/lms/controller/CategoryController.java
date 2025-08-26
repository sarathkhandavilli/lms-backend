package com.lms.controller;

import com.lms.dto.CategoryDto;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.UpdateCategoryDto;
import com.lms.service.CategoryService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    // adds category (admin can add)
    @PostMapping("/add")
    public ResponseEntity<CommonApiResponse> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    // updates the category (admin can update)
    @PutMapping("/update")
    public ResponseEntity<CommonApiResponse> updateCategory(@RequestBody UpdateCategoryDto updatecategoryDto) {
        return categoryService.updateCategory(updatecategoryDto);
    }

    //deactivates the category (admin can delete)
    @DeleteMapping("/delete")
    public ResponseEntity<CommonApiResponse> deleteCategory(@RequestParam("categoryId") int categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    // fetches all the categories (any one can) to filter courses.
    @GetMapping("/fetch/all")
    public ResponseEntity<CommonApiResponse> getAllCategories(@RequestParam("status") String status) {

        CommonApiResponse response;

        try {

        List<CategoryDto> categoryDtos = categoryService.getAllCategories(status);
        if (categoryDtos == null) {
            response = new CommonApiResponse(false, "Categories not found", null);
            logger.warn("No categories found with status: {}", status);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
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
