package com.lms.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CommonApiResponse {

    private boolean isSuccess;
    private String message;
    private Object data;

}
