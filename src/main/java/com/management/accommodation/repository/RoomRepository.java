package com.management.accommodation.repository;

import com.management.accommodation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Integer> {
    Optional<Room> findByRoom(String room);
}
