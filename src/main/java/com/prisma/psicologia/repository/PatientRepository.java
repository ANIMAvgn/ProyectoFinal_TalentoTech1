package com.prisma.psicologia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prisma.psicologia.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}