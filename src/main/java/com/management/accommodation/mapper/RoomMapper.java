package com.management.accommodation.mapper;

import com.management.accommodation.dto.requestdto.RoomDto;
import com.management.accommodation.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room addUpdateRoomMapper(Room room,RoomDto roomDto){
        room.setRoom(roomDto.getRoom());
        room.setBuilding(roomDto.getBuilding());
        room.setTotalSpace(roomDto.getTotalSpace());
        room.setAvailableSpace(roomDto.getAvailableSpace());
        room.setFilledSpace(roomDto.getFilledSpace());
        return room;
    }

    public RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoom(room.getRoom());
        roomDto.setBuilding(room.getBuilding());
        roomDto.setAvailableSpace(room.getAvailableSpace());
        roomDto.setTotalSpace(room.getTotalSpace());
        roomDto.setFilledSpace(room.getFilledSpace());
        return roomDto;
    }

}
