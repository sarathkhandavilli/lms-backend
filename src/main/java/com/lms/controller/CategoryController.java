package com.lms.controller;

import com.lms.dto.CategoryDto;
import com.lms.dto.CommonApiResponse;
import com.lms.dto.UpdateCategoryDto;
import com.lms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

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
        return categoryService.getAllCategories(status);
    }
}
