package com.management.accommodation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
