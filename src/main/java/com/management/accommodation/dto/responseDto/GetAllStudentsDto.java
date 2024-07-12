package com.management.accommodation.dto.responseDto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GetAllStudentsDto {
    private String studentId;
    private String name;
    private String email;
    private String roomNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String status;
}
