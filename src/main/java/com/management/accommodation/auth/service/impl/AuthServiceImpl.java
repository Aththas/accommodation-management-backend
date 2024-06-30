package com.management.accommodation.auth.service.impl;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import com.management.accommodation.auth.dto.responseDto.ResponseDto;
import com.management.accommodation.auth.entity.Role;
import com.management.accommodation.auth.entity.User;
import com.management.accommodation.auth.repository.AuthRepository;
import com.management.accommodation.auth.service.AuthService;
import com.management.accommodation.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        Optional<User> optionalUser = authRepository.findByEmail(registerDto.getEmail());
        if(optionalUser.isPresent())
            return new ResponseEntity<>("Email Already Existed", HttpStatus.NOT_ACCEPTABLE);

        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.ADMIN);
        authRepository.save(user);

        final String accessToken = jwtService.generateAccessToken(user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setAccessToken(accessToken);

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> authenticate(AuthDto authDto) {
        Optional<User> optionalUser = authRepository.findByEmail(authDto.getEmail());
        if(optionalUser.isPresent()){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDto.getEmail(),
                            authDto.getPassword()
                    )
            );

            User user = optionalUser.get();
            final String accessToken = jwtService.generateAccessToken(user);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setAccessToken(accessToken);

            return new ResponseEntity<>(responseDto,HttpStatus.OK);

        }
        return new ResponseEntity<>("Authentication Failed",HttpStatus.UNAUTHORIZED);
    }
}
