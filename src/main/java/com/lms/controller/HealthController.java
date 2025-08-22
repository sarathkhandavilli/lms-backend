package com.lms.controller;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public ResponseEntity<?> health(HttpServletRequest request) {
        logger.info("✅ Health endpoint called with method: {} at {}", request.getMethod(), LocalDateTime.now());
        return ResponseEntity.ok(Map.of(
            "message", "UP",
            "success", true
        ));
    }

}
