package com.management.accommodation.controller;

import com.management.accommodation.dto.requestDto.RoomDto;
import com.management.accommodation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<String> addRoom(@RequestBody RoomDto roomDto){
        return roomService.addRoom(roomDto);
    }

    @PutMapping
    public ResponseEntity<String> updateRoom(@RequestParam Integer id, @RequestBody RoomDto roomDto){
        return roomService.updateRoom(id,roomDto);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteRoom(@RequestParam Integer id){
        return roomService.deleteRoom(id);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> viewRooms(){
        return roomService.viewRoom();
    }

}
