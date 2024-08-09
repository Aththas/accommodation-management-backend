package com.management.accommodation.service.impl;

import com.management.accommodation.dto.requestdto.RoomDto;
import com.management.accommodation.entity.Room;
import com.management.accommodation.entity.Staff;
import com.management.accommodation.entity.Student;
import com.management.accommodation.mapper.RoomMapper;
import com.management.accommodation.repository.RoomRepository;
import com.management.accommodation.repository.StaffAccommodationRepository;
import com.management.accommodation.repository.StudentAccommodationRepository;
import com.management.accommodation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final StaffAccommodationRepository staffAccommodationRepository;
    private final StudentAccommodationRepository studentAccommodationRepository;
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

    @Override
    public ResponseEntity<String> revokeStaffRoomSpace(Integer id) {
        Optional<Staff> optionalStaff = staffAccommodationRepository.findById(id);
        if(optionalStaff.isPresent() && optionalStaff.get().getStatus().equals("accepted")){
            Staff staff = optionalStaff.get();
            if(staff.getEndDate().before(new Date())){
                Optional<Room> optionalRoom = roomRepository.findByRoom(staff.getRoomNo());
                if(optionalRoom.isPresent()){
                    Room room = optionalRoom.get();
                    room.setFilledSpace(room.getFilledSpace() - 1);
                    room.setAvailableSpace(room.getAvailableSpace() + 1);
                    roomRepository.save(room);
                    return new ResponseEntity<>("Room Space Updated",HttpStatus.OK);
                }
                return new ResponseEntity<>("Invalid Room Request",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Accommodation Not Yet Expired",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Invalid Staff Accommodation",HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> revokeMaleStudentRoomSpace(Integer id) {
        return revokeStudentRoomSpace(id,"male");
    }

    @Override
    public ResponseEntity<String> revokeFemaleStudentRoomSpace(Integer id) {
        return revokeStudentRoomSpace(id,"female");
    }

    public ResponseEntity<String> revokeStudentRoomSpace(Integer id, String gender){
        Optional<Student> optionalStudent = studentAccommodationRepository.findById(id);
        if(optionalStudent.isPresent() && optionalStudent.get().getStatus().equals("accepted") && optionalStudent.get().getGender().equals(gender)){
            Student student = optionalStudent.get();
            if(student.getEndDate().before(new Date())){
                Optional<Room> optionalRoom = roomRepository.findByRoom(student.getRoomNo());
                if(optionalRoom.isPresent()){
                    Room room = optionalRoom.get();
                    room.setFilledSpace(room.getFilledSpace() - 1);
                    room.setAvailableSpace(room.getAvailableSpace() + 1);
                    roomRepository.save(room);
                    return new ResponseEntity<>("Room Space Updated",HttpStatus.OK);
                }
                return new ResponseEntity<>("Invalid Room Request",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Accommodation Not Yet Expired",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Invalid Student Accommodation",HttpStatus.NOT_FOUND);
    }


}
