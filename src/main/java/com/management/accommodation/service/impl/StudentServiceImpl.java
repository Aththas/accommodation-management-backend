package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.entity.Student;
import com.management.accommodation.repository.StudentRepository;
import com.management.accommodation.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    @Override
    public ResponseEntity<String> accommodation(StudentDto studentDto) throws IOException {
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

        studentRepository.save(student);
        return new ResponseEntity<>("Accommodation Details has been sent to " + studentDto.getEmail(), HttpStatus.OK);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
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
