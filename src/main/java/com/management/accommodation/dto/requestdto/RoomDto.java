package com.management.accommodation.dto.requestdto;

import lombok.Data;

@Data
public class RoomDto {
    private Integer id;
    private String room;
    private Integer totalSpace;
    private Integer filledSpace;
    private Integer availableSpace;
    private String building;
}
