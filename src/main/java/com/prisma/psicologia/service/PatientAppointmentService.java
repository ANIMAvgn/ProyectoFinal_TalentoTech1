package com.prisma.psicologia.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prisma.psicologia.dto.BookApointmentRequest;
import com.prisma.psicologia.dto.BookAppointmentFromSlotRequest;
import com.prisma.psicologia.dto.PatientAppointmentItemResponse;
import com.prisma.psicologia.model.Appointment;
import com.prisma.psicologia.model.AppointmentStatus;
import com.prisma.psicologia.model.Patient;
import com.prisma.psicologia.model.Professional;
import com.prisma.psicologia.model.ProfessionalMonthlyShift;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.model.WorkShift;
import com.prisma.psicologia.repository.AppointmentRepository;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalMonthlyShiftRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientAppointmentService {

    private static final ZoneId CO_ZONE = ZoneId.of("America/Bogota");

    // ✅ Formato claro para mostrar fecha y hora
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final ProfessionalMonthlyShiftRepository monthlyShiftRepository;
    private final AppointmentRepository appointmentRepository;

    private Patient getLoggedPatient(String email) {
        User user = userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"PATIENT".equals(user.getRole().getName())) {
            throw new RuntimeException("Acceso denegado: no es PATIENT");
        }

        return patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil paciente no encontrado"));
    }

    // ✅ VER MIS CITAS
    public List<PatientAppointmentItemResponse> getMyAppointments(String email) {

        Patient patient = getLoggedPatient(email);

        return appointmentRepository
                .findByPatientIdOrderByStartAtAsc(patient.getId())
                .stream()
                .map(appt -> new PatientAppointmentItemResponse(
                        appt.getId(),
                        appt.getStartAt()
                                .atZoneSameInstant(CO_ZONE)
                                .format(DATE_FORMAT),
                        appt.getDurationMinutes(),
                        appt.getStatus().name(),
                        appt.getProfessional().getId(),
                        appt.getProfessional().getUser().getNombre(),
                        appt.getProfessional().getUser().getApellido(),
                        appt.getProfessional().getUser().getEmail()
                ))
                .toList();
    }

    // ✅ RESERVAR CITA
    @Transactional
    public void bookAppointment(String email, BookApointmentRequest request) {

        Patient patient = getLoggedPatient(email);

        // Parse startAt ISO
        OffsetDateTime startAt;
        try {
            startAt = OffsetDateTime.parse(request.getStartAt());
        } catch (Exception e) {
            throw new RuntimeException("Formato startAt inválido. Usa ISO: 2026-03-10T07:00:00-05:00");
        }

        // Normalizar a zona Colombia
        ZonedDateTime startCol = startAt.atZoneSameInstant(CO_ZONE);
        LocalDate date = startCol.toLocalDate();
        LocalTime time = startCol.toLocalTime().withSecond(0).withNano(0);

        // 1) Solo lunes a viernes
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Solo se permiten citas de lunes a viernes");
        }

        // 2) Solo horas en punto
        if (time.getMinute() != 0) {
            throw new RuntimeException("Las citas solo se pueden agendar a las horas en punto");
        }

        // 3) Profesional debe existir
        Professional professional = professionalRepository.findById(request.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

        // 4) Debe tener jornada configurada para ese mes
        int year = date.getYear();
        int month = date.getMonthValue();

        WorkShift shift = monthlyShiftRepository
                .findByProfessionalIdAndYearAndMonth(professional.getId(), year, month)
                .map(ProfessionalMonthlyShift::getShift)
                .orElseThrow(() -> new RuntimeException("Este profesional no tiene horario configurado para ese mes"));

        // 5) Validar que la hora esté permitida por la jornada
        if (!isAllowedTime(shift, time)) {
            throw new RuntimeException("La hora no está dentro del horario del profesional para ese mes");
        }

        // 6) Máximo 4 citas al mes
        OffsetDateTime monthStart = date.withDayOfMonth(1).atStartOfDay(CO_ZONE).toOffsetDateTime();
        OffsetDateTime monthEnd = date.with(TemporalAdjusters.firstDayOfNextMonth()).atStartOfDay(CO_ZONE).toOffsetDateTime();

        long countMonth = appointmentRepository.countByPatientIdAndStartAtBetweenAndStatusIn(
                patient.getId(), monthStart, monthEnd, List.of(AppointmentStatus.BOOKED)
        );
        if (countMonth >= 4) {
            throw new RuntimeException("No puedes agendar más de 4 citas en un mes");
        }

        // 7) Máximo 1 cita por semana
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        LocalDate weekEndExclusive = weekStart.plusDays(7);

        OffsetDateTime weekStartDT = weekStart.atStartOfDay(CO_ZONE).toOffsetDateTime();
        OffsetDateTime weekEndDT = weekEndExclusive.atStartOfDay(CO_ZONE).toOffsetDateTime();

        long countWeek = appointmentRepository.countByPatientIdAndStartAtBetweenAndStatusIn(
                patient.getId(), weekStartDT, weekEndDT, List.of(AppointmentStatus.BOOKED)
        );
        if (countWeek >= 1) {
            throw new RuntimeException("Solo puedes agendar 1 cita por semana");
        }

        // 8) Evitar double booking
        OffsetDateTime startFinal = startCol.toOffsetDateTime();

        boolean taken = appointmentRepository.existsByProfessionalIdAndStartAtAndStatusIn(
                professional.getId(), startFinal, List.of(AppointmentStatus.BOOKED)
        );
        if (taken) {
            throw new RuntimeException("Ese horario ya está ocupado");
        }

        // 9) Guardar cita
        try {
            Appointment appt = new Appointment();
            appt.setProfessional(professional);
            appt.setPatient(patient);
            appt.setStartAt(startFinal);
            appt.setDurationMinutes(45);
            appt.setStatus(AppointmentStatus.BOOKED);

            appointmentRepository.save(appt);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Ese horario ya está ocupado (otro paciente lo reservó primero).");
        }
    }

    // ✅ RESERVAR DESDE SLOT
    public void bookFromSlot(String email, BookAppointmentFromSlotRequest request) {

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime time = LocalTime.parse(request.getTime());

        OffsetDateTime startAt = date.atTime(time)
                .atZone(CO_ZONE)
                .toOffsetDateTime();

        BookApointmentRequest req = new BookApointmentRequest();
        req.setProfessionalId(request.getProfessionalId());
        req.setStartAt(startAt.toString());

        bookAppointment(email, req);
    }

    private boolean isAllowedTime(WorkShift shift, LocalTime time) {
        if (shift == WorkShift.MORNING) {
            return time.equals(LocalTime.of(7, 0)) ||
                   time.equals(LocalTime.of(8, 0)) ||
                   time.equals(LocalTime.of(9, 0)) ||
                   time.equals(LocalTime.of(10, 0)) ||
                   time.equals(LocalTime.of(11, 0));
        }

        if (shift == WorkShift.AFTERNOON) {
            return time.equals(LocalTime.of(13, 0)) ||
                   time.equals(LocalTime.of(14, 0)) ||
                   time.equals(LocalTime.of(15, 0)) ||
                   time.equals(LocalTime.of(16, 0)) ||
                   time.equals(LocalTime.of(17, 0));
        }

        return isAllowedTime(WorkShift.MORNING, time) || isAllowedTime(WorkShift.AFTERNOON, time);
    }

    // ✅ CANCELAR CITA
    public void cancelAppointment(String email, Long appointmentId) {
        Patient patient = getLoggedPatient(email);

        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!appt.getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("Acceso denegado: la cita no pertenece a este paciente");
        }

        if (appt.getStatus() != AppointmentStatus.BOOKED) {
            throw new RuntimeException("Solo puedes cancelar citas en estado BOOKED");
        }

        OffsetDateTime now = OffsetDateTime.now(CO_ZONE);
        OffsetDateTime start = appt.getStartAt();

        long hours = Duration.between(now, start).toHours();

        if (hours < 24) {
            throw new RuntimeException("No puedes cancelar una cita faltando menos de 24 horas");
        }

        appt.setStatus(AppointmentStatus.CANCELLED);
        appt.setCancelledAt(now);
        appointmentRepository.save(appt);
    }
}

// Canelacion de citas desde el paciente

// @PostMapping("/{id}/cancel")

// POST http://localhost:8081/patient/appointments/{id}/cancel

// Authorization: Bearer TOKEN_DEL_PROFESIONAL

//Visualizacion de lista de citas reservadas desde paciente

// Get http://localhost:8081/patient/appointments

// Authorization: Bearer TOKEN_DEL_PROFESIONAL

