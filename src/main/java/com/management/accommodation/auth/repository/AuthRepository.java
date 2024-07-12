package com.management.accommodation.auth.repository;

import com.management.accommodation.auth.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
