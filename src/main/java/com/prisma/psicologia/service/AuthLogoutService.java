package com.prisma.psicologia.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.prisma.psicologia.model.BlacklistedToken;
import com.prisma.psicologia.repository.BlacklistedTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthLogoutService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtService jwtService;

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no enviado");
        }

        String token = authHeader.substring(7);

        if (blacklistedTokenRepository.existsByToken(token)) {
            return;
        }

        BlacklistedToken blacklisted = new BlacklistedToken();
        blacklisted.setToken(token);
        blacklisted.setExpiresAt(jwtService.extractExpiration(token));
        blacklisted.setCreatedAt(OffsetDateTime.now());

        blacklistedTokenRepository.save(blacklisted);
    }
}