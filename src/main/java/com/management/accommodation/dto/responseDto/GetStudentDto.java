package com.management.accommodation.dto.responseDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GetStudentDto {
    private String studentId;
    private String name;
    private String email;
    private Integer contactNo;
    private String faculty;
    private String roomNo;
    private String studentType;
    private String paymentSlip;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private Integer noOfDays;
    private String status;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
