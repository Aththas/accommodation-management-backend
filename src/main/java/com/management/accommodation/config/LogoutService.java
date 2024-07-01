package com.management.accommodation.config;

import com.management.accommodation.auth.entity.token.Token;
import com.management.accommodation.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        final String jwt = authHeader.substring(7);

        Optional<Token> optionalToken = tokenRepository.findByToken(jwt);
        if(optionalToken.isPresent()){
            Token token = optionalToken.get();
            token.setRevoked(true);
            tokenRepository.save(token);
        }
    }
}
