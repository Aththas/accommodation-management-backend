package com.management.accommodation.controller;

import com.management.accommodation.dto.requestdto.*;
import com.management.accommodation.dto.responsedto.GetAllStaffsDto;
import com.management.accommodation.dto.responsedto.GetAllStudentsDto;
import com.management.accommodation.dto.responsedto.GetStaffDto;
import com.management.accommodation.dto.responsedto.GetStudentDto;
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
    public ResponseEntity<GetStudentDto> getStudentAccommodation(@RequestParam Integer id) {
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

    @GetMapping("/admin/getAllStaffAccommodations")//admin only
    public ResponseEntity<List<GetAllStaffsDto>> getAllStaffAccommodations(){
        return accommodationService.getAllStaffAccommodations();
    }

    @GetMapping("/admin/getStaffAccommodation")//admin only
    public ResponseEntity<GetStaffDto> getStaffAccommodation(@RequestParam Integer id){
        return accommodationService.getStaffAccommodation(id);
    }

    @PutMapping("/admin/staff-update-status")//admin only
    public ResponseEntity<String> updateStaffAccommodation(@RequestParam Integer id, @RequestBody UpdateStaffStatusDto updateStaffStatusDto){
        return accommodationService.updateStaffAccommodation(id,updateStaffStatusDto);
    }

    @PutMapping("/admin/male-student-update-status")//Boy warden only
    public ResponseEntity<String> updateMaleStudentAccommodation(@RequestParam Integer id, @RequestBody UpdateStudentStatusDto updateStudentStatusDto){
        return accommodationService.updateMaleStudentAccommodation(id,updateStudentStatusDto);
    }

    @PutMapping("/admin/female-student-update-status")//Girl Warden only
    public ResponseEntity<String> updateFemaleStudentAccommodation(@RequestParam Integer id, @RequestBody UpdateStudentStatusDto updateStudentStatusDto){
        return accommodationService.updateFemaleStudentAccommodation(id,updateStudentStatusDto);
    }

}
