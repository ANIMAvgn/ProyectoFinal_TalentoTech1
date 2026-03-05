package com.prisma.psicologia.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prisma.psicologia.model.ProfessionalMonthlyShift;

public interface ProfessionalMonthlyShiftRepository extends JpaRepository<ProfessionalMonthlyShift, Long> {

    Optional<ProfessionalMonthlyShift> findByProfessionalIdAndYearAndMonth(
        Long professionalId,
        Integer year,
        Integer month
    );

    List<ProfessionalMonthlyShift> findByYearAndMonth(Integer year, Integer month);
}