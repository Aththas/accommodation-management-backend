package com.management.accommodation.auth.service.impl;

import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import com.management.accommodation.auth.entity.user.User;
import com.management.accommodation.auth.repository.AuthRepository;
import com.management.accommodation.auth.service.UserService;
import com.management.accommodation.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectValidator<PasswordResetDto> passwordResetValidator;
    @Override
    public ResponseEntity<String> passwordReset(PasswordResetDto passwordResetDto) {
        passwordResetValidator.validate(passwordResetDto);

        final String userEmail = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        User user = authRepository.findByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User Not Found"));

        if(!passwordEncoder.matches(passwordResetDto.getPassword(), user.getPassword())){
            return new ResponseEntity<>("Invalid Current Password",HttpStatus.UNAUTHORIZED);
        }
        if(!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmPassword())){
            return new ResponseEntity<>("Password Confirmation Error",HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        authRepository.save(user);
        return new ResponseEntity<>("Password Update Successfully",HttpStatus.OK);
    }
}
