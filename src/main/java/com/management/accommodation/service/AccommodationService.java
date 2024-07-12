package com.management.accommodation.service;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StaffDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AccommodationService {

    ResponseEntity<String> studentAccommodation(StudentDto studentDto) throws IOException;

    ResponseEntity<String> staffAccommodation(StaffDto staffDto) throws IOException;

    ResponseEntity<String> verifyOtpStudent(OtpDto otpDto);

    ResponseEntity<String> verifyOtpStaff(OtpDto otpDto);
}
