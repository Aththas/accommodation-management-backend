package com.management.accommodation.auth.dto.responseDto;

import lombok.Data;

@Data
public class ResponseDto {
    private String accessToken;
    private String refreshToken;
}
