package com.management.accommodation.controller;

import com.management.accommodation.dto.requestDto.StudentDto;
import com.management.accommodation.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> accommodation(@ModelAttribute StudentDto studentDto) throws IOException {
        return studentService.accommodation(studentDto);
    }
}
