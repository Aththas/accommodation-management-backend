package com.management.accommodation.service;

import com.management.accommodation.dto.requestdto.RoomDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomService {
    ResponseEntity<String> addRoom(RoomDto roomDto);

    ResponseEntity<String> updateRoom(Integer id, RoomDto roomDto);

    ResponseEntity<String> deleteRoom(Integer id);

    ResponseEntity<List<RoomDto>> viewRoom();

    ResponseEntity<String> revokeStaffRoomSpace(Integer id);

    ResponseEntity<String> revokeMaleStudentRoomSpace(Integer id);

    ResponseEntity<String> revokeFemaleStudentRoomSpace(Integer id);
}
