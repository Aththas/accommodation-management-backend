package com.management.accommodation.auth.service;

import com.management.accommodation.auth.dto.requestDto.ForgotPasswordDto;
import com.management.accommodation.auth.dto.requestDto.NewPasswordDto;
import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import com.management.accommodation.dto.requestdto.OtpDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> passwordReset(PasswordResetDto passwordResetDto);

    ResponseEntity<String> forgotPassword(ForgotPasswordDto forgotPasswordDto);

    ResponseEntity<String> verifyOtp(OtpDto otpDto);

    ResponseEntity<String> newPassword(NewPasswordDto newPasswordDto);
}
