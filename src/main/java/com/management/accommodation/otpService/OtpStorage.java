package com.management.accommodation.otpService;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpStorage<T> {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, T> accommodationStorage = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);
    }

    public String retrieveOtp(String email) {
        return otpStorage.get(email);
    }

    public void removeOtp(String email) {
        otpStorage.remove(email);
    }

    public void storeAccommodationDetails(String email, T object) {
        accommodationStorage.put(email, object);
    }

    public T retrieveAccommodationDetails(String email) {
        return accommodationStorage.get(email);
    }

    public void removeAccommodationDetails(String email) {
        accommodationStorage.remove(email);
    }
}

