package com.management.accommodation.auth.service;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);

    ResponseEntity<?> authenticate(AuthDto authDto);

    ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response);
}
