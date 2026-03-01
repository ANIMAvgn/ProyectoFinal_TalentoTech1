package com.prisma.psicologia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prisma.psicologia.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
 
    
}
