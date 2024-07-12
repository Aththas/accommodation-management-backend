package com.management.accommodation.otpService;

import java.util.concurrent.ConcurrentHashMap;

import com.management.accommodation.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OtpStorage {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Student> studentStorage = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);
    }

    public String retrieveOtp(String email) {
        return otpStorage.get(email);
    }

    public void removeOtp(String email) {
        otpStorage.remove(email);
    }

    public void storeStudentDetails(String email, Student student) {
        studentStorage.put(email, student);
    }

    public Student retrieveStudentDetails(String email) {
        return studentStorage.get(email);
    }

    public void removeStudentDetails(String email) {
        studentStorage.remove(email);
    }
}

