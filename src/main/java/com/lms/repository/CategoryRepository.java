package com.lms.repository;

import com.lms.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository {


    Optional<Category> findById(int id);

    List<Category> findAll(String status);

    boolean deleteById(int categoryId);

    Category add(Category category);
}
