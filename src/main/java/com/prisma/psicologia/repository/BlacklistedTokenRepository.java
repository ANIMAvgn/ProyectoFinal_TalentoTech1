package com.prisma.psicologia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prisma.psicologia.model.BlacklistedToken;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);
}