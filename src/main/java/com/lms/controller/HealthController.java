package com.lms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.CommonApiResponse;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<CommonApiResponse> health() {
        CommonApiResponse response;
        response = new CommonApiResponse(true, "UP", null);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
