package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.emailService.EmailService;
import com.management.accommodation.entity.Room;
import com.management.accommodation.otpService.OtpStorage;
import com.management.accommodation.otpService.OtpUtil;
import com.management.accommodation.entity.Student;
import com.management.accommodation.repository.RoomRepository;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final OtpStorage otpStorage;
    private final EmailService emailService;
    private final RoomRepository roomRepository;
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

        Optional<Room> optionalRoom = roomRepository.findByRoom(studentDto.getRoomNo());
        if(optionalRoom.isEmpty()){
            return new ResponseEntity<>("Invalid Room No",HttpStatus.BAD_REQUEST);
        }

        Room room = optionalRoom.get();
        if(room.getAvailableSpace() == 0){
            return new ResponseEntity<>("Room is already Filled",HttpStatus.BAD_REQUEST);
        }

        if(studentDto.getGender().equals("male") && !room.getBuilding().equals("B8")){
            return new ResponseEntity<>("Please select male student room",HttpStatus.BAD_REQUEST);
        }
        if(studentDto.getGender().equals("female") && !room.getBuilding().equals("B9")){
            return new ResponseEntity<>("Please select female student room",HttpStatus.BAD_REQUEST);
        }

        // Generate and send OTP
        String otp = OtpUtil.generateOtp();
        otpStorage.storeOtp(studentDto.getEmail(), otp);
        otpStorage.storeStudentDetails(studentDto.getEmail(), student);
        emailService.sendEmail(studentDto.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
        return new ResponseEntity<>("OTP sent to email " + studentDto.getEmail(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyOtp(OtpDto otpDto) {
        String storedOtp = otpStorage.retrieveOtp(otpDto.getEmail());

        if (storedOtp != null && storedOtp.equals(otpDto.getOtp())) {
            Student student = otpStorage.retrieveStudentDetails(otpDto.getEmail());
            if (student != null) {

                studentRepository.save(student);
                emailService.sendEmail(
                        otpDto.getEmail(),
                        "Regarding Student Accommodation",
                        "Your Accommodation has been sent for the admin review. Please wait for the approval." +
                                " You will Receive a confirmation mail with in 24 hours");
                otpStorage.removeOtp(otpDto.getEmail());
                otpStorage.removeStudentDetails(otpDto.getEmail());

                return new ResponseEntity<>("OTP verified successfully. More details will be sent to " + otpDto.getEmail(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No student accommodation details found.", HttpStatus.NOT_FOUND);
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
