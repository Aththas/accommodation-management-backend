package com.management.accommodation.auth.dto.requestDto;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
