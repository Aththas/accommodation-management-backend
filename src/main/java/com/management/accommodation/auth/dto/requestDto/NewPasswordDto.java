package com.management.accommodation.auth.dto.requestDto;

import lombok.Data;

@Data
public class NewPasswordDto {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
