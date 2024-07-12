package com.management.accommodation.dto.responseDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GetStaffDto {
    private String staffId;
    private String name;
    private String post;
    private String contactNo;
    private String email;
    private String roomNo;
    private String staffType;
    private String paymentSlip;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    private Integer noOfDays;
    private String status;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
