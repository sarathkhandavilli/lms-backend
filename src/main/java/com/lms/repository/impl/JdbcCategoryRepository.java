package com.lms.repository.impl;

import com.lms.model.Category;
import com.lms.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCategoryRepository implements CategoryRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Category> findById(int id) {

        String sql = "SELECT * FROM category WHERE id = ? AND status = ? ";

        try {
            Category category = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                return c;
            }, id,"ACTIVE");
            return Optional.ofNullable(category);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAll(String status) {
        
        String sql = "SELECT * FROM category c WHERE status = ? ORDER BY c.name ";

        List<Category> categories =  jdbcTemplate.query(sql, (rs, rowNum) -> {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            category.setStatus(rs.getString("status"));
            return category;
        },status);

        return categories;

    }

    @Override
    public boolean deleteById(int categoryId) {
        String sql = "UPDATE category SET status = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,"INACTIVE",categoryId);

        return rowsAffected > 0;
    }

    @Override
    public Category add(Category category) {

        if (category.getId() == 0) {
            String sql = "INSERT INTO category (name, description, status) VALUES (?,?,?) RETURNING id";
            Integer generatedId = jdbcTemplate.queryForObject(sql, Integer.class,
                category.getName(),
                category.getDescription(),
                category.getStatus()
            );
            category.setId(generatedId);
        } else {
            String sql = "UPDATE category SET name = ?, description = ?  WHERE id = ?";
            jdbcTemplate.update(sql,
                category.getName(),
                category.getDescription(),
                category.getId()
            );
        }

        return category;

    }
}
