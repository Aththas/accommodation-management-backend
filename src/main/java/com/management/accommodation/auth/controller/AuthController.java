package com.management.accommodation.auth.controller;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import com.management.accommodation.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthDto authDto){
        return authService.authenticate(authDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        return authService.refresh(request,response);
    }
}
