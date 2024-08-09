package com.management.accommodation.dto.responsedto;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GetAllStudentsDto {
    private Integer id;
    private String name;
    private String email;
    private String roomNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String status;
}
