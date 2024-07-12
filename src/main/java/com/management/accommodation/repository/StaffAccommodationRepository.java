package com.management.accommodation.repository;

import com.management.accommodation.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffAccommodationRepository extends JpaRepository<Staff,Integer> {
}
