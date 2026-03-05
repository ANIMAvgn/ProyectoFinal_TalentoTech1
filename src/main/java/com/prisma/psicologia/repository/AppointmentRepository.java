package com.prisma.psicologia.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prisma.psicologia.model.Appointment;
import com.prisma.psicologia.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


      List<Appointment> findByProfessionalIdAndStartAtBetweenAndStatus(
        Long professionalId,
        OffsetDateTime start,
        OffsetDateTime end,
        AppointmentStatus status
    );

    List<Appointment> findByProfessionalIdAndStartAtBetweenAndStatusIn(
        Long professionalId,
        OffsetDateTime start,
        OffsetDateTime end,
        List<AppointmentStatus> statuses
    );

    boolean existsByProfessionalIdAndStartAtAndStatusIn(
        Long professionalId,
        OffsetDateTime startAt,
        List<AppointmentStatus> statuses
    );

    long countByPatientIdAndStartAtBetweenAndStatusIn(
        Long patientId,
        OffsetDateTime start,
        OffsetDateTime end,
        List<AppointmentStatus> statuses
    );

   
}
