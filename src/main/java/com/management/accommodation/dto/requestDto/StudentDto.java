package com.management.accommodation.dto.requestDto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StudentDto {
    private String studentId;
    private String name;
    private String email;
    private Integer contactNo;
    private String faculty;
    private Integer roomNo;
    private String studentType;
    private MultipartFile paymentSlip;
}
