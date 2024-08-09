package com.management.accommodation.mapper;

import com.management.accommodation.dto.requestdto.StaffDto;
import com.management.accommodation.dto.requestdto.StudentDto;
import com.management.accommodation.dto.responsedto.GetAllStaffsDto;
import com.management.accommodation.dto.responsedto.GetAllStudentsDto;
import com.management.accommodation.dto.responsedto.GetStaffDto;
import com.management.accommodation.dto.responsedto.GetStudentDto;
import com.management.accommodation.entity.Staff;
import com.management.accommodation.entity.Student;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;

@Component
public class AccommodationMapper {

    public GetAllStudentsDto convertToDto(Student student) {
        GetAllStudentsDto getAllStudentsDto = new GetAllStudentsDto();
        getAllStudentsDto.setId(student.getId());
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
        getStudentDto.setId(student.getStudentId());
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
        getAllStaffsDto.setId(staff.getId());
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
        getStaffDto.setId(staff.getStaffId());
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

    public Student studentAccommodationMapper(StudentDto studentDto) throws IOException {
        Student student = new Student();
        student.setStudentId(studentDto.getStudentId());
        student.setStudentType(studentDto.getStudentType());
        student.setFaculty(studentDto.getFaculty());
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setContactNo(studentDto.getContactNo());
        student.setRoomNo(studentDto.getRoomNo());

        MultipartFile file = studentDto.getPaymentSlip();
        String paymentSlip = saveFile(file);
        student.setPaymentSlip(paymentSlip);

        student.setStartDate(studentDto.getStartDate());
        student.setNoOfDays(studentDto.getNoOfDays());
        student.setStatus(studentDto.getStatus());
        student.setGender(studentDto.getGender());

        // Calculate end date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(studentDto.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, studentDto.getNoOfDays());
        Date endDate = calendar.getTime();
        student.setEndDate(endDate);

        return student;
    }

    public Staff staffAccommodationMapper(StaffDto staffDto) throws IOException {
        Staff staff = new Staff();
        staff.setStaffId(staffDto.getStaffId());
        staff.setName(staffDto.getName());
        staff.setGender(staffDto.getGender());
        staff.setEmail(staffDto.getEmail());
        staff.setStaffType(staffDto.getStaffType());
        staff.setContactNo(staffDto.getContactNo());
        staff.setPost(staffDto.getPost());
        staff.setRoomNo(staffDto.getRoomNo());
        staff.setStartDate(staffDto.getStartDate());
        staff.setNoOfDays(staffDto.getNoOfDays());
        staff.setStatus(staffDto.getStatus());

        MultipartFile file = staffDto.getPaymentSlip();
        String paymentSlip = saveFile(file);
        staff.setPaymentSlip(paymentSlip);

        // Calculate end date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(staffDto.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, staffDto.getNoOfDays());
        Date endDate = calendar.getTime();
        staff.setEndDate(endDate);
        return staff;
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file==null && file.isEmpty()) {
            throw new IllegalArgumentException("Cannot save empty file.");
        }
        String baseDir = "C:\\Assignments_and_Notes\\9. springboot\\Nuha\\accommodation\\";
        String folderPath = "src\\main\\java\\com\\management\\accommodation\\paymentSlip\\";
        Path path = Paths.get(folderPath);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Path targetPath = Paths.get(folderPath + file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get(baseDir, folderPath, file.getOriginalFilename()).toString();
    }
}
