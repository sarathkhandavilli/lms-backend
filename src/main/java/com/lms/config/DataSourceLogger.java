package com.lms.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceLogger {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void logJdbcUrl() {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            String url = connection.getMetaData().getURL();  // Get the URL from Connection metadata
            System.out.println("Current JDBC URL: " + url);  // Log the JDBC URL
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error fetching JDBC URL: " + e.getMessage());
        }
    }
}
