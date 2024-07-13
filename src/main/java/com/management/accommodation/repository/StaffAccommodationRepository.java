package com.management.accommodation.repository;

import com.management.accommodation.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAccommodationRepository extends JpaRepository<Staff,Integer> {
    List<Staff> findAllByOrderByIdAsc();
}
