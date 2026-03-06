package com.prisma.psicologia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.BookAppointmentFromSlotRequest;
import com.prisma.psicologia.model.*;
import com.prisma.psicologia.repository.AppointmentRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessionalAppointmentService {

    private static final ZoneId CO_ZONE = ZoneId.of("America/Bogota");

    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;
    private final AppointmentRepository appointmentRepository;

    private Professional getLoggedProfessional(String email) {
        User user = userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"PROFESSIONAL".equals(user.getRole().getName())) {
            throw new RuntimeException("Acceso denegado: no es PROFESSIONAL");
        }

        return professionalRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil profesional no encontrado"));
    }

    public void cancelAppointment(String email, Long appointmentId) {
        Professional prof = getLoggedProfessional(email);

        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!appt.getProfessional().getId().equals(prof.getId())) {
            throw new RuntimeException("Acceso denegado: la cita no pertenece a este profesional");
        }

        if (appt.getStatus() != AppointmentStatus.BOOKED) {
            throw new RuntimeException("Solo puedes cancelar citas en estado BOOKED");
        }

        appt.setStatus(AppointmentStatus.CANCELLED);
        appt.setCancelledAt(OffsetDateTime.now(CO_ZONE));
        appointmentRepository.save(appt);
    }

    
   
}

// Cancelacion de citas desde el profesional

// POST /professional/appointments/{id}/cancel