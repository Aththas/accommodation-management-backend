package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.emailVerification.EmailService;
import com.management.accommodation.emailVerification.OtpStorage;
import com.management.accommodation.emailVerification.OtpUtil;
import com.management.accommodation.entity.Student;
import com.management.accommodation.repository.StudentRepository;
import com.management.accommodation.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final OtpStorage otpStorage;
    private final EmailService emailService;
    //private final OtpUtil otpUtil;
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

        // Generate and send OTP
        String otp = OtpUtil.generateOtp();
        otpStorage.storeOtp(studentDto.getEmail(), otp);
        otpStorage.storeStudentDetails(studentDto.getEmail(), student);
        emailService.sendOtpEmail(studentDto.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
        return new ResponseEntity<>("OTP sent to email " + studentDto.getEmail(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyOtp(OtpDto otpDto) {
        String storedOtp = otpStorage.retrieveOtp(otpDto.getEmail());
        log.error(storedOtp);
        if (storedOtp != null && storedOtp.equals(otpDto.getOtp())) {
            Student student = otpStorage.retrieveStudentDetails(otpDto.getEmail());
            if (student != null) {
                // Save student details to the database
                studentRepository.save(student);
                otpStorage.removeOtp(otpDto.getEmail());
                otpStorage.removeStudentDetails(otpDto.getEmail());

                return new ResponseEntity<>("OTP verified successfully. Student details saved.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No student details found.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_GATEWAY);
        }
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
