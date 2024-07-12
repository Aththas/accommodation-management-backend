package com.management.accommodation.auth.repository;

import com.management.accommodation.auth.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Query("""
            select t from Token t
            where t.user.id = :userId
            and t.revoked=false
            """)
    List<Token> findAllValidTokensByUserId(Integer userId);

    Optional<Token> findByToken(String token);
}
