package com.management.accommodation.controller;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StaffDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/accommodation")
@RequiredArgsConstructor
@Slf4j
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping("/student")
    public ResponseEntity<String> studentAccommodation(@ModelAttribute StudentDto studentDto) throws IOException {
        return accommodationService.studentAccommodation(studentDto);
    }

    @PostMapping("/staff")
    public ResponseEntity<String> staffAccommodation(@ModelAttribute StaffDto staffDto) throws IOException {
        return accommodationService.staffAccommodation(staffDto);
    }

    @PostMapping("/student/otp-verify")
    public ResponseEntity<String> otpVerifyStudent(@RequestBody OtpDto otpDto) {
        return accommodationService.verifyOtpStudent(otpDto);
    }

    @PostMapping("/staff/otp-verify")
    public ResponseEntity<String> otpVerifyStaff(@RequestBody OtpDto otpDto) {
        return accommodationService.verifyOtpStaff(otpDto);
    }
}
