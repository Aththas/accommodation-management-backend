package com.management.accommodation.controller;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StaffDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.dto.responseDto.GetAllStudentsDto;
import com.management.accommodation.dto.responseDto.GetStudentDto;
import com.management.accommodation.entity.Student;
import com.management.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/admin/getAllMaleStudentAccommodations")//male only
    public ResponseEntity<List<GetAllStudentsDto>> getAllMaleStudentAccommodations(){
        return accommodationService.getAllMaleStudentAccommodations();
    }

    @GetMapping("/admin/getAllFemaleStudentAccommodations")//female only
    public ResponseEntity<List<GetAllStudentsDto>> getAllFemaleStudentAccommodations(){
        return accommodationService.getAllFemaleStudentAccommodations();
    }

    @GetMapping("/admin/getAllStudentAccommodations")//admin only
    public ResponseEntity<List<GetAllStudentsDto>> getAllStudentAccommodations(){
        return accommodationService.getAllStudentAccommodations();
    }

    @GetMapping("/admin/getStudentAccommodation")//admin only
    public ResponseEntity<GetStudentDto> getStudentAccommodation(@RequestParam Integer id){
        return accommodationService.getStudentAccommodation(id);
    }

    @GetMapping("/admin/getMaleStudentAccommodation")//male only
    public ResponseEntity<GetStudentDto> getMaleStudentAccommodation(@RequestParam Integer id){
        return accommodationService.getMaleStudentAccommodation(id);
    }

    @GetMapping("/admin/getFemaleStudentAccommodation")//female only
    public ResponseEntity<GetStudentDto> getFemaleStudentAccommodation(@RequestParam Integer id){
        return accommodationService.getFemaleStudentAccommodation(id);
    }

}
