package com.management.accommodation.mapper;

import com.management.accommodation.dto.responseDto.GetAllStaffsDto;
import com.management.accommodation.dto.responseDto.GetAllStudentsDto;
import com.management.accommodation.dto.responseDto.GetStaffDto;
import com.management.accommodation.dto.responseDto.GetStudentDto;
import com.management.accommodation.entity.Staff;
import com.management.accommodation.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

    public GetAllStudentsDto convertToDto(Student student) {
        GetAllStudentsDto getAllStudentsDto = new GetAllStudentsDto();
        getAllStudentsDto.setStudentId(student.getStudentId());
        getAllStudentsDto.setName(student.getName());
        getAllStudentsDto.setEmail(student.getEmail());
        getAllStudentsDto.setRoomNo(student.getRoomNo());
        getAllStudentsDto.setStartDate(student.getStartDate());
        getAllStudentsDto.setEndDate(student.getEndDate());
        getAllStudentsDto.setStatus(student.getStatus());
        return getAllStudentsDto;
    }

    public GetStudentDto convertToGetStudentDto(Student student)
    {
        GetStudentDto getStudentDto = new GetStudentDto();
        getStudentDto.setStudentId(student.getStudentId());
        getStudentDto.setGender(student.getGender());
        getStudentDto.setFaculty(student.getFaculty());
        getStudentDto.setStudentType(student.getStudentType());
        getStudentDto.setEmail(student.getEmail());
        getStudentDto.setName(student.getName());
        getStudentDto.setStatus(student.getStatus());
        getStudentDto.setContactNo(student.getContactNo());
        getStudentDto.setEndDate(student.getEndDate());
        getStudentDto.setStartDate(student.getStartDate());
        getStudentDto.setPaymentSlip(student.getPaymentSlip());
        getStudentDto.setNoOfDays(student.getNoOfDays());
        getStudentDto.setRoomNo(student.getRoomNo());
        return getStudentDto;
    }

    public GetAllStaffsDto convertToGetAllStaffsDto(Staff staff) {
        GetAllStaffsDto getAllStaffsDto = new GetAllStaffsDto();
        getAllStaffsDto.setStaffId(staff.getStaffId());
        getAllStaffsDto.setName(staff.getName());
        getAllStaffsDto.setEmail(staff.getEmail());
        getAllStaffsDto.setRoomNo(staff.getRoomNo());
        getAllStaffsDto.setStartDate(staff.getStartDate());
        getAllStaffsDto.setEndDate(staff.getEndDate());
        getAllStaffsDto.setStatus(staff.getStatus());
        return getAllStaffsDto;
    }

    public GetStaffDto convertToGetStaffDto(Staff staff)
    {
        GetStaffDto getStaffDto = new GetStaffDto();
        getStaffDto.setName(staff.getName());
        getStaffDto.setGender(staff.getGender());
        getStaffDto.setStaffId(staff.getStaffId());
        getStaffDto.setEmail(staff.getEmail());
        getStaffDto.setContactNo(staff.getContactNo());
        getStaffDto.setStaffType(staff.getStaffType());
        getStaffDto.setPost(staff.getPost());
        getStaffDto.setPaymentSlip(staff.getPaymentSlip());
        getStaffDto.setStartDate(staff.getStartDate());
        getStaffDto.setEndDate(staff.getEndDate());
        getStaffDto.setNoOfDays(staff.getNoOfDays());
        getStaffDto.setRoomNo(staff.getRoomNo());
        getStaffDto.setStatus(staff.getStatus());
        return getStaffDto;
    }
}
