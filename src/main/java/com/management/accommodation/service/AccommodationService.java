package com.management.accommodation.service;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StaffDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.dto.requestDto.UpdateStaffStatusDto;
import com.management.accommodation.dto.responseDto.GetAllStaffsDto;
import com.management.accommodation.dto.responseDto.GetAllStudentsDto;
import com.management.accommodation.dto.responseDto.GetStaffDto;
import com.management.accommodation.dto.responseDto.GetStudentDto;
import com.management.accommodation.entity.Student;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface AccommodationService {

    ResponseEntity<String> studentAccommodation(StudentDto studentDto) throws IOException;

    ResponseEntity<String> staffAccommodation(StaffDto staffDto) throws IOException;

    ResponseEntity<String> verifyOtpStudent(OtpDto otpDto);

    ResponseEntity<String> verifyOtpStaff(OtpDto otpDto);

    ResponseEntity<List<GetAllStudentsDto>> getAllMaleStudentAccommodations();

    ResponseEntity<List<GetAllStudentsDto>> getAllFemaleStudentAccommodations();

    ResponseEntity<List<GetAllStudentsDto>> getAllStudentAccommodations();

    ResponseEntity<GetStudentDto> getStudentAccommodation(Integer id);

    ResponseEntity<GetStudentDto> getMaleStudentAccommodation(Integer id);

    ResponseEntity<GetStudentDto> getFemaleStudentAccommodation(Integer id);

    ResponseEntity<List<GetAllStaffsDto>> getAllStaffAccommodations();

    ResponseEntity<GetStaffDto> getStaffAccommodation(Integer id);

    ResponseEntity<String> updateStaffAccommodation(Integer id, UpdateStaffStatusDto updateStaffStatusDto);
}
