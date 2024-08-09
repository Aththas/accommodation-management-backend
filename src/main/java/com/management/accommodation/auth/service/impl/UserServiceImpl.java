package com.management.accommodation.auth.service.impl;

import com.management.accommodation.auth.dto.requestDto.ForgotPasswordDto;
import com.management.accommodation.auth.dto.requestDto.NewPasswordDto;
import com.management.accommodation.auth.dto.requestDto.PasswordResetDto;
import com.management.accommodation.auth.entity.user.User;
import com.management.accommodation.auth.repository.AuthRepository;
import com.management.accommodation.auth.service.UserService;
import com.management.accommodation.dto.requestdto.OtpDto;
import com.management.accommodation.emailservice.EmailService;
import com.management.accommodation.otpService.OtpStorage;
import com.management.accommodation.otpService.OtpUtil;
import com.management.accommodation.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectValidator<PasswordResetDto> passwordResetValidator;
    private final OtpStorage<User> userOtpStorage;
    private final EmailService emailService;
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

    @Override
    public ResponseEntity<String> forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        Optional<User> optionalUser = authRepository.findByEmail(forgotPasswordDto.getEmail());
        if(optionalUser.isPresent()){
            String otp = OtpUtil.generateOtp();
            userOtpStorage.storeOtp(forgotPasswordDto.getEmail(), otp);
            emailService.sendEmail(forgotPasswordDto.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
            return new ResponseEntity<>("OTP sent to email " + forgotPasswordDto.getEmail(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid User",HttpStatus.FORBIDDEN);

    }

    @Override
    public ResponseEntity<String> verifyOtp(OtpDto otpDto) {
        String storedOtp = userOtpStorage.retrieveOtp(otpDto.getEmail());

        if (storedOtp != null && storedOtp.equals(otpDto.getOtp())) {
            userOtpStorage.removeOtp(otpDto.getEmail());
            return new ResponseEntity<>("OTP Verified", HttpStatus.OK);//should re-direct to the hidden new password page
        }
        return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_GATEWAY);

    }

    @Override
    public ResponseEntity<String> newPassword(NewPasswordDto newPasswordDto) {

        if(!newPasswordDto.getNewPassword().equals(newPasswordDto.getConfirmPassword())){
            return new ResponseEntity<>("Password Confirmation Error",HttpStatus.BAD_REQUEST);
        }

        String userEmail = newPasswordDto.getEmail();
        Optional<User> optionalUser = authRepository.findByEmail(userEmail);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
            authRepository.save(user);
            return new ResponseEntity<>("Password Update Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid User",HttpStatus.FORBIDDEN);
    }
}
