package com.prisma.psicologia.repository;

import com.prisma.psicologia.*;
import com.prisma.psicologia.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNumeroDocumento(String numeroDocumento);

}
