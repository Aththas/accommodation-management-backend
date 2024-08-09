package com.management.accommodation.dto.requestdto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class StudentDto {
    private String studentId;
    private String name;
    private String email;
    private Integer contactNo;
    private String faculty;
    private String roomNo;
    private String studentType;
    private MultipartFile paymentSlip;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private Integer noOfDays;
    private String status;
    private String gender;
}
