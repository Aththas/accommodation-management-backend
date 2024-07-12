package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestDto.OtpDto;
import com.management.accommodation.dto.requestDto.StaffDto;
import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.dto.responseDto.GetAllStudentsDto;
import com.management.accommodation.dto.responseDto.GetStudentDto;
import com.management.accommodation.emailService.EmailService;
import com.management.accommodation.entity.Room;
import com.management.accommodation.entity.Staff;
import com.management.accommodation.mapper.AccommodationMapper;
import com.management.accommodation.otpService.OtpStorage;
import com.management.accommodation.otpService.OtpUtil;
import com.management.accommodation.entity.Student;
import com.management.accommodation.repository.RoomRepository;
import com.management.accommodation.repository.StaffAccommodationRepository;
import com.management.accommodation.repository.StudentAccommodationRepository;
import com.management.accommodation.service.AccommodationService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final StudentAccommodationRepository studentAccommodationRepository;
    private final StaffAccommodationRepository staffAccommodationRepository;
    private final OtpStorage<Student> studentStorage;
    private final OtpStorage<Staff> staffStorage;
    private final EmailService emailService;
    private final RoomRepository roomRepository;
    private final AccommodationMapper accommodationMapper;

    @Override
    public ResponseEntity<String> studentAccommodation(StudentDto studentDto) throws IOException {
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
        studentStorage.storeOtp(studentDto.getEmail(), otp);
        studentStorage.storeAccommodationDetails(studentDto.getEmail(), student);
        emailService.sendEmail(studentDto.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
        return new ResponseEntity<>("OTP sent to email " + studentDto.getEmail(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyOtpStudent(OtpDto otpDto) {
        String storedOtp = studentStorage.retrieveOtp(otpDto.getEmail());

        if (storedOtp != null && storedOtp.equals(otpDto.getOtp())) {
            Student student = studentStorage.retrieveAccommodationDetails(otpDto.getEmail());
            if (student != null) {
                studentAccommodationRepository.save(student);
                emailService.sendEmail(
                        otpDto.getEmail(),
                        "Regarding Student Accommodation",
                        "Your Accommodation has been sent for the admin review. Please wait for the approval." +
                                " You will Receive a confirmation mail with in 24 hours.");
                studentStorage.removeOtp(otpDto.getEmail());
                studentStorage.removeAccommodationDetails(otpDto.getEmail());

                return new ResponseEntity<>("OTP verified successfully. More details will be sent to " + otpDto.getEmail(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No student accommodation details found.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Invalid OTP.", HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    public ResponseEntity<String> staffAccommodation(StaffDto staffDto) throws IOException {
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

        Optional<Room> optionalRoom = roomRepository.findByRoom(staffDto.getRoomNo());
        if(optionalRoom.isEmpty()){
            return new ResponseEntity<>("Invalid Room No",HttpStatus.BAD_REQUEST);
        }

        Room room = optionalRoom.get();
        if(room.getAvailableSpace() == 0){
            return new ResponseEntity<>("Room is already Filled",HttpStatus.BAD_REQUEST);
        }

        if(!room.getBuilding().equals("B17")){
            return new ResponseEntity<>("Please select lecturer room",HttpStatus.BAD_REQUEST);
        }


        // Generate and send OTP
        String otp = OtpUtil.generateOtp();
        staffStorage.storeOtp(staffDto.getEmail(), otp);
        staffStorage.storeAccommodationDetails(staffDto.getEmail(), staff);
        emailService.sendEmail(staffDto.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
        return new ResponseEntity<>("OTP sent to email " + staffDto.getEmail(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyOtpStaff(OtpDto otpDto) {
        String storedOtp = staffStorage.retrieveOtp(otpDto.getEmail());

        if (storedOtp != null && storedOtp.equals(otpDto.getOtp())) {
            Staff staff = staffStorage.retrieveAccommodationDetails(otpDto.getEmail());
            if (staff != null) {
                staffAccommodationRepository.save(staff);
                emailService.sendEmail(
                        otpDto.getEmail(),
                        "Regarding Student Accommodation",
                        "Your Accommodation has been sent for the admin review. Please wait for the approval." +
                                " You will Receive a confirmation mail with in 24 hours.");
                staffStorage.removeOtp(otpDto.getEmail());
                staffStorage.removeAccommodationDetails(otpDto.getEmail());

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

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllMaleStudentAccommodations() {
        return getStudentsByGender("male");
    }

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllFemaleStudentAccommodations() {
        return getStudentsByGender("female");
    }

    private ResponseEntity<List<GetAllStudentsDto>> getStudentsByGender(String gender){
        List<Student> students = studentAccommodationRepository.findAllByGender(gender);
        return new ResponseEntity<>(students.stream().map(accommodationMapper::convertToDto).collect(Collectors.toList()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllStudentAccommodations() {
        List<Student> students = studentAccommodationRepository.findAll();
        return new ResponseEntity<>(students.stream().map(accommodationMapper::convertToDto).collect(Collectors.toList()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetStudentDto> getStudentAccommodation(Integer id) {
        Optional<Student> optionalStudent = studentAccommodationRepository.findById(id);
        if(optionalStudent.isPresent()){
            Student student = optionalStudent.get();
            return  new ResponseEntity<>(accommodationMapper.convertToGetStudentDto(student),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GetStudentDto> getMaleStudentAccommodation(Integer id) {
        return getStudentByGender("male",id);
    }

    @Override
    public ResponseEntity<GetStudentDto> getFemaleStudentAccommodation(Integer id) {
        return getStudentByGender("female",id);
    }

    private ResponseEntity<GetStudentDto> getStudentByGender(String gender,Integer id){
        Optional<Student> optionalStudent = studentAccommodationRepository.findById(id);
        if(optionalStudent.isPresent()){
            Student student = optionalStudent.get();
            if(student.getGender().equals(gender)){
                return  new ResponseEntity<>(accommodationMapper.convertToGetStudentDto(student),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
