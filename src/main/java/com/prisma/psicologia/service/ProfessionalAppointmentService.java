package com.prisma.psicologia.service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.ProfessionalAppointmentItemResponse;
import com.prisma.psicologia.model.Appointment;
import com.prisma.psicologia.model.AppointmentStatus;
import com.prisma.psicologia.model.Professional;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.repository.AppointmentRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessionalAppointmentService {

    private static final ZoneId CO_ZONE = ZoneId.of("America/Bogota");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    // ✅ VER MIS CITAS
    public List<ProfessionalAppointmentItemResponse> getMyAppointments(String email) {

        Professional professional = getLoggedProfessional(email);

        return appointmentRepository
                .findByProfessionalIdOrderByStartAtAsc(professional.getId())
                .stream()
                .map(appt -> new ProfessionalAppointmentItemResponse(
                        appt.getId(),
                        appt.getStartAt()
                                .atZoneSameInstant(CO_ZONE)
                                .format(DATE_FORMAT),
                        appt.getDurationMinutes(),
                        appt.getStatus().name(),
                        appt.getPatient().getId(),
                        appt.getPatient().getUser().getNombre(),
                        appt.getPatient().getUser().getApellido(),
                        appt.getPatient().getUser().getEmail()
                ))
                .toList();
    }

    // ✅ CANCELAR CITA
    public void cancelAppointment(String email, Long appointmentId) {
        Professional professional = getLoggedProfessional(email);

        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!appt.getProfessional().getId().equals(professional.getId())) {
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

//Authorization: Bearer TOKEN_DEL_PROFESIONAL

// Ver Listado de citas

// GET http://localhost:8081/professional/appointments

// http://localhost:8081/professional/appointments