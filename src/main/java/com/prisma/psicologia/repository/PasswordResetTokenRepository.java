package com.prisma.psicologia.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prisma.psicologia.model.PasswordResetToken;
import com.prisma.psicologia.model.User;

public interface PasswordResetTokenRepository 
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> 
    findTopByUserAndCodigoAndUsadoFalseOrderByExpiracionDesc(
            User user, String codigo);
}
