package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestDto.RoomDto;
import com.management.accommodation.entity.Room;
import com.management.accommodation.mapper.RoomMapper;
import com.management.accommodation.repository.RoomRepository;
import com.management.accommodation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    @Override
    public ResponseEntity<String> addRoom(RoomDto roomDto) {
        Optional<Room> optionalRoom = roomRepository.findByRoom(roomDto.getRoom());
        if(optionalRoom.isPresent()){
            return new ResponseEntity<>("Room is already exist", HttpStatus.ALREADY_REPORTED);
        }
        Room room = new Room();
        roomRepository.save(roomMapper.addUpdateRoomMapper(room,roomDto));

        return new ResponseEntity<>("Room added successfully",HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> updateRoom(Integer id, RoomDto roomDto) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isPresent()){
            Room room = optionalRoom.get();
            roomRepository.save(roomMapper.addUpdateRoomMapper(room,roomDto));
            return new ResponseEntity<>("Room Updated successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Room Not Exist",HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> deleteRoom(Integer id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isPresent()){
            Room room = optionalRoom.get();
            room.setAvailableSpace(0);
            room.setFilledSpace(room.getTotalSpace());
            roomRepository.save(room);
            return new ResponseEntity<>("Room Deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Room Not Exist",HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<RoomDto>> viewRoom() {
        List<Room> rooms = roomRepository.findAllByOrderByIdAsc();
        return new ResponseEntity<>(rooms.stream().map(roomMapper::convertToDto).collect(Collectors.toList()) ,HttpStatus.OK);
    }

}
