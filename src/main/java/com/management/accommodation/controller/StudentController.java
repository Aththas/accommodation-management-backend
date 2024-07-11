package com.management.accommodation.controller;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> accommodation(@ModelAttribute StudentDto studentDto) throws IOException {
        return studentService.accommodation(studentDto);
    }

    @PostMapping("/otp-verify")
    public ResponseEntity<String> otpVerify(@RequestBody OtpDto otpDto) throws IOException {
        return studentService.verifyOtp(otpDto);
    }
}
