package com.management.accommodation.auth.controller;

import com.management.accommodation.auth.dto.requestDto.ForgotPasswordDto;
import com.management.accommodation.auth.dto.requestDto.NewPasswordDto;
import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import com.management.accommodation.auth.service.UserService;
import com.management.accommodation.dto.requestdto.OtpDto;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        return userService.forgotPassword(forgotPasswordDto);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpDto otpDto){
        return userService.verifyOtp(otpDto);
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody NewPasswordDto newPasswordDto){
        return userService.newPassword(newPasswordDto);
    }
}
