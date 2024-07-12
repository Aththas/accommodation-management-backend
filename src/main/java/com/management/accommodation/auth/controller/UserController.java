package com.management.accommodation.auth.controller;

import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import com.management.accommodation.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/password-reset")
    public ResponseEntity<String> passwordReset(@RequestBody PasswordResetDto passwordResetDto){
        return userService.passwordReset(passwordResetDto);
    }
}
