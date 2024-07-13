package com.management.accommodation.dto.requestDto;

import lombok.Data;

@Data
public class RoomDto {
    private String room;
    private Integer totalSpace;
    private Integer filledSpace;
    private Integer availableSpace;
    private String building;
}
