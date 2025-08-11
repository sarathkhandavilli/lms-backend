package com.lms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ForgotPassword {
    
    private int fpid;

    private int otp;
    
    private Date expirationTime;

    private int userId;
}
