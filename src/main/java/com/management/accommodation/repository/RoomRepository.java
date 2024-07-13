package com.management.accommodation.repository;

import com.management.accommodation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    Optional<Room> findByRoom(String room);
    List<Room> findAllByOrderByIdAsc();
}
