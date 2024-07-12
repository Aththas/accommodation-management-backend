package com.management.accommodation.dto.requestDto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class StaffDto {
    private String staffId;
    private String name;
    private String email;
    private String contactNo;
    private String post;
    private String roomNo;
    private String staffType;
    private MultipartFile paymentSlip;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private Integer noOfDays;
    private String status;
    private String gender;
}
