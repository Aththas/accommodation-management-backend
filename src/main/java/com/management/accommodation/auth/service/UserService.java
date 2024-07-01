package com.management.accommodation.auth.service;

import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> passwordReset(PasswordResetDto passwordResetDto);
}
