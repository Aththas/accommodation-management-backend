package com.management.accommodation.auth.service.impl;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import com.management.accommodation.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        return null;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthDto authDto) {
        return null;
    }
}
