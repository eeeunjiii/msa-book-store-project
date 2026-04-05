package com.example.jwt;

import com.example.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByEmail(String email);
    Optional<RefreshToken> findByJti(String jti);
    void deleteByEmail(String email);
}
