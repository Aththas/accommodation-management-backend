package com.management.accommodation.repository;

import com.management.accommodation.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccommodationRepository extends JpaRepository<Student,Integer> {
}
