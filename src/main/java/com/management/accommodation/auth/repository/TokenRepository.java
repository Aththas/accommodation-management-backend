package com.management.accommodation.auth.repository;

import com.management.accommodation.auth.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
}
