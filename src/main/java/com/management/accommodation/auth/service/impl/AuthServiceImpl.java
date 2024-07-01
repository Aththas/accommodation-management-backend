package com.management.accommodation.auth.service.impl;

import com.management.accommodation.auth.dto.requestDto.AuthDto;
import com.management.accommodation.auth.dto.requestDto.RegisterDto;
import com.management.accommodation.auth.dto.responseDto.ResponseDto;
import com.management.accommodation.auth.entity.token.Token;
import com.management.accommodation.auth.entity.token.TokenType;
import com.management.accommodation.auth.entity.user.Role;
import com.management.accommodation.auth.entity.user.User;
import com.management.accommodation.auth.repository.AuthRepository;
import com.management.accommodation.auth.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;
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
        saveToken(accessToken,user);

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
            saveToken(accessToken,user);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setAccessToken(accessToken);

            return new ResponseEntity<>(responseDto,HttpStatus.OK);

        }
        return new ResponseEntity<>("Authentication Failed",HttpStatus.UNAUTHORIZED);
    }

    private void saveToken(String accessToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
