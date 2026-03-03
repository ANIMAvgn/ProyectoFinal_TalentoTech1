package com.prisma.psicologia.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prisma.psicologia.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    Optional<Professional> findByUserId(Long userId);
}