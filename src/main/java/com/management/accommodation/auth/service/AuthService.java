package com.management.accommodation.auth.service;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterDto registerDto);

    ResponseEntity<?> authenticate(AuthDto authDto);
}
