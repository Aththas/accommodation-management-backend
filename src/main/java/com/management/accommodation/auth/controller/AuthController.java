package com.management.accommodation.auth.controller;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import com.management.accommodation.auth.entity.user.User;
import com.management.accommodation.auth.repository.AuthRepository;
import com.management.accommodation.auth.service.AuthService;
import com.management.accommodation.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthRepository authRepository;

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

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);
        Optional<User> user = authRepository.findByEmail(email);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND));
    }
}
