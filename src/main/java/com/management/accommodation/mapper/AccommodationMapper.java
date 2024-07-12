package com.management.accommodation.mapper;

import com.management.accommodation.dto.responseDto.GetAllStudentsDto;
import com.management.accommodation.dto.responseDto.GetStudentDto;
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
}
