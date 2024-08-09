package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestdto.*;
import com.management.accommodation.dto.responsedto.GetAllStaffsDto;
import com.management.accommodation.dto.responsedto.GetAllStudentsDto;
import com.management.accommodation.dto.responsedto.GetStaffDto;
import com.management.accommodation.dto.responsedto.GetStudentDto;
import com.management.accommodation.emailservice.EmailService;
import com.management.accommodation.entity.Room;
import com.management.accommodation.entity.Staff;
import com.management.accommodation.entity.Student;
import com.management.accommodation.mapper.AccommodationMapper;
import com.management.accommodation.otpService.OtpStorage;
import com.management.accommodation.otpService.OtpUtil;
import com.management.accommodation.repository.RoomRepository;
import com.management.accommodation.repository.StaffAccommodationRepository;
import com.management.accommodation.repository.StudentAccommodationRepository;
import com.management.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    private static final String INVALID_ROOM_NO = "Invalid Room No";
    private static final String MALE = "male";
    private static final String FEMALE = "female";
    private static final String SUBJECT_FOT_STUDENT = "Regarding Student Accommodation";
    private static final String SUBJECT_FOT_STAFF = "Regarding Staff Accommodation";

    @Override
    public ResponseEntity<String> studentAccommodation(StudentDto studentDto) throws IOException {
        Student student =  accommodationMapper.studentAccommodationMapper(studentDto);

        Optional<Room> optionalRoom = roomRepository.findByRoom(studentDto.getRoomNo());
        if(optionalRoom.isEmpty()){
            return new ResponseEntity<>(INVALID_ROOM_NO,HttpStatus.BAD_REQUEST);
        }

        Room room = optionalRoom.get();
        if(room.getAvailableSpace() == 0){
            return new ResponseEntity<>("Room is already Filled",HttpStatus.BAD_REQUEST);
        }

        if(studentDto.getGender().equals(MALE) && !room.getBuilding().equals("B8")){
            return new ResponseEntity<>("Please select male student room",HttpStatus.BAD_REQUEST);
        }
        if(studentDto.getGender().equals(FEMALE) && !room.getBuilding().equals("B9")){
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
                Optional<Room> optionalRoom = roomRepository.findByRoom(student.getRoomNo());
                if(optionalRoom.isPresent()){
                    Room room = optionalRoom.get();
                    room.setFilledSpace(room.getFilledSpace() + 1);
                    room.setAvailableSpace(room.getAvailableSpace() - 1);
                    roomRepository.save(room);
                }

                emailService.sendEmail(
                        otpDto.getEmail(),
                        SUBJECT_FOT_STUDENT,
                        "Your Accommodation has been sent for the admin review. Please wait for the approval." +
                                "You will Receive a confirmation mail with in 24 hours.");
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
        Staff staff = accommodationMapper.staffAccommodationMapper(staffDto);

        Optional<Room> optionalRoom = roomRepository.findByRoom(staffDto.getRoomNo());
        if(optionalRoom.isEmpty()){
            return new ResponseEntity<>(INVALID_ROOM_NO,HttpStatus.BAD_REQUEST);
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
                Optional<Room> optionalRoom = roomRepository.findByRoom(staff.getRoomNo());
                if(optionalRoom.isPresent()){
                    Room room = optionalRoom.get();
                    room.setFilledSpace(room.getFilledSpace() + 1);
                    room.setAvailableSpace(room.getAvailableSpace() - 1);
                    roomRepository.save(room);
                }

                emailService.sendEmail(
                        otpDto.getEmail(),
                        SUBJECT_FOT_STAFF,
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

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllMaleStudentAccommodations() {
        return getStudentsByGender(MALE);
    }

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllFemaleStudentAccommodations() {
        return getStudentsByGender(FEMALE);
    }

    private ResponseEntity<List<GetAllStudentsDto>> getStudentsByGender(String gender){
        List<Student> students = studentAccommodationRepository.findAllByGenderOrderByIdAsc(gender);
        return new ResponseEntity<>(students.stream().map(accommodationMapper::convertToDto).toList(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GetAllStudentsDto>> getAllStudentAccommodations() {
        List<Student> students = studentAccommodationRepository.findAllByOrderByIdAsc();
        return new ResponseEntity<>(students.stream().map(accommodationMapper::convertToDto).toList(),HttpStatus.OK);
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
        return getStudentByGender(MALE,id);
    }

    @Override
    public ResponseEntity<GetStudentDto> getFemaleStudentAccommodation(Integer id) {
        return getStudentByGender(FEMALE,id);
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

    @Override
    public ResponseEntity<List<GetAllStaffsDto>> getAllStaffAccommodations() {
        List<Staff> staffs = staffAccommodationRepository.findAllByOrderByIdAsc();
        return new ResponseEntity<>(staffs.stream().map(accommodationMapper::convertToGetAllStaffsDto).toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetStaffDto> getStaffAccommodation(Integer id) {
        Optional<Staff> optionalStaff = staffAccommodationRepository.findById(id);
        if(optionalStaff.isPresent()){
            Staff staff = optionalStaff.get();
            return  new ResponseEntity<>(accommodationMapper.convertToGetStaffDto(staff),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> updateStaffAccommodation(Integer id, UpdateStaffStatusDto updateStaffStatusDto) {
        Optional<Staff> optionalStaff = staffAccommodationRepository.findById(id);
        if(optionalStaff.isPresent() && optionalStaff.get().getStatus().equals("pending")){
            Staff staff = optionalStaff.get();
            staff.setStatus(updateStaffStatusDto.getStatus());
            staffAccommodationRepository.save(staff);

            if(updateStaffStatusDto.getStatus().equals("rejected")){
                String roomNo = staff.getRoomNo();
                Optional<Room> optionalRoom = roomRepository.findByRoom(roomNo);
                if(optionalRoom.isPresent()){
                    Room room = optionalRoom.get();
                    room.setFilledSpace(room.getFilledSpace() - 1);
                    room.setAvailableSpace(room.getAvailableSpace() + 1);
                    roomRepository.save(room);

                    emailService.sendEmail(
                            staff.getEmail(),
                            SUBJECT_FOT_STAFF,
                            "Your Accommodation has been Rejected." +
                                    " Contact Admin for more details and your accommodation fee will be refund soon");
                }
                else{
                    return new ResponseEntity<>(INVALID_ROOM_NO,HttpStatus.NOT_FOUND);
                }
            }
            else if(updateStaffStatusDto.getStatus().equals("accepted")){
                emailService.sendEmail(
                        staff.getEmail(),
                        SUBJECT_FOT_STAFF,
                        "Your Accommodation has been accepted. Your Room No: "+ staff.getRoomNo() +
                                " Your accommodation will be from "+ staff.getStartDate() +" to "+ staff.getEndDate() +
                                " Show this email to the receptionist to get your room keys.");
            }
            return new ResponseEntity<>("Status Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Status Updated Failed",HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> updateMaleStudentAccommodation(Integer id, UpdateStudentStatusDto updateStudentStatusDto) {
        return updateStudentAccommodation(id, updateStudentStatusDto, MALE);
    }

    @Override
    public ResponseEntity<String> updateFemaleStudentAccommodation(Integer id, UpdateStudentStatusDto updateStudentStatusDto) {
        return updateStudentAccommodation(id, updateStudentStatusDto, FEMALE);
    }

    private ResponseEntity<String> updateStudentAccommodation(Integer id, UpdateStudentStatusDto updateStudentStatusDto, String gender) {
        Optional<Student> optionalStudent = studentAccommodationRepository.findById(id);
        if(optionalStudent.isPresent() && optionalStudent.get().getStatus().equals("pending") && optionalStudent.get().getGender().equals(gender)){
            Student student = optionalStudent.get();
            student.setStatus(updateStudentStatusDto.getStatus());
            studentAccommodationRepository.save(student);

            if(updateStudentStatusDto.getStatus().equals("rejected")){
                String roomNo = student.getRoomNo();
                Optional<Room> optionalRoom = roomRepository.findByRoom(roomNo);
                if(optionalRoom.isPresent()){
                    Room room = new Room();
                    room.setFilledSpace(room.getFilledSpace() - 1);
                    room.setAvailableSpace(room.getAvailableSpace() + 1);
                    roomRepository.save(room);

                    emailService.sendEmail(
                            student.getEmail(),
                            SUBJECT_FOT_STUDENT,
                            "Your Accommodation has been Rejected." +
                                    " Contact Admin for more details and your accommodation fee will be refund soon");
                }
                else{
                    return new ResponseEntity<>(INVALID_ROOM_NO,HttpStatus.NOT_FOUND);
                }
            }
            else if(updateStudentStatusDto.getStatus().equals("accepted")){
                emailService.sendEmail(
                        student.getEmail(),
                        SUBJECT_FOT_STUDENT,
                        "Your Accommodation has been accepted. Your Room No: "+ student.getRoomNo() +
                                " Your accommodation will be from "+ student.getStartDate() +" to "+ student.getEndDate() +
                                " Show this email to the receptionist to get your room keys.");
            }
            return new ResponseEntity<>("Status Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Status Updated Failed",HttpStatus.NOT_FOUND);
    }

}
