package com.management.accommodation.service;

import com.management.accommodation.dto.requestDto.StudentDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface StudentService {
    ResponseEntity<String> accommodation(StudentDto studentDto) throws IOException;
}
