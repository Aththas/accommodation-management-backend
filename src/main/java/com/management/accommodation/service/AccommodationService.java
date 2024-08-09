package com.management.accommodation.service;

import com.management.accommodation.dto.requestdto.*;
import com.management.accommodation.dto.responsedto.GetAllStaffsDto;
import com.management.accommodation.dto.responsedto.GetAllStudentsDto;
import com.management.accommodation.dto.responsedto.GetStaffDto;
import com.management.accommodation.dto.responsedto.GetStudentDto;
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

    ResponseEntity<String> updateMaleStudentAccommodation(Integer id, UpdateStudentStatusDto updateStudentStatusDto);

    ResponseEntity<String> updateFemaleStudentAccommodation(Integer id, UpdateStudentStatusDto updateStudentStatusDto);
}
