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
import com.management.accommodation.validation.ObjectValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectValidator<RegisterDto> registerValidator;
    private final ObjectValidator<AuthDto> authValidator;
    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        registerValidator.validate(registerDto);
        Optional<User> optionalUser = authRepository.findByEmail(registerDto.getEmail());
        if(optionalUser.isPresent())
            return new ResponseEntity<>("Email Already Existed", HttpStatus.NOT_ACCEPTABLE);

        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if (registerDto.getRole().equalsIgnoreCase("BOY_WARDEN"))
            user.setRole(Role.BOY_WARDEN);
        if (registerDto.getRole().equalsIgnoreCase("GIRL_WARDEN"))
            user.setRole(Role.GIRL_WARDEN);
        if (registerDto.getRole().equalsIgnoreCase("ADMIN"))
            user.setRole(Role.ADMIN);
        authRepository.save(user);

        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        saveToken(accessToken,user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshToken);

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> authenticate(AuthDto authDto) {
        authValidator.validate(authDto);
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
            final String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllValidTokens(user.getId());
            saveToken(accessToken,user);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setAccessToken(accessToken);
            responseDto.setRefreshToken(refreshToken);

            return new ResponseEntity<>(responseDto,HttpStatus.OK);

        }
        return new ResponseEntity<>("Authentication Failed",HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return new ResponseEntity<>("Invalid Header",HttpStatus.BAD_REQUEST);

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        if(userEmail != null){
            Optional<User> optionalUser = authRepository.findByEmail(userEmail);
            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                if(jwtService.isTokenValid(jwt,user)){
                    final String accessToken = jwtService.generateRefreshToken(user);
                    revokeAllValidTokens(user.getId());
                    saveToken(accessToken,user);

                    ResponseDto responseDto = new ResponseDto();
                    responseDto.setAccessToken(accessToken);
                    responseDto.setRefreshToken(jwt);

                    return new ResponseEntity<>(responseDto,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Invalid Token",HttpStatus.FORBIDDEN);
    }

    private void revokeAllValidTokens(Integer id) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUserId(id);
        if(validTokens.isEmpty())
            return;

        validTokens.forEach(
                token -> token.setRevoked(true)
        );
        tokenRepository.saveAll(validTokens);
    }

    private void saveToken(String accessToken, User user) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
