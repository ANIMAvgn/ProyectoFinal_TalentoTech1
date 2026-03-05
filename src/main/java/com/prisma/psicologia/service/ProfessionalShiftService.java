package com.prisma.psicologia.service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prisma.psicologia.model.*;
import com.prisma.psicologia.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessionalShiftService {

    private static final ZoneId CO_ZONE = ZoneId.of("America/Bogota");

    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;
    private final ProfessionalMonthlyShiftRepository monthlyShiftRepository;
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

    /**
     * Configura la jornada para el MES SIGUIENTE.
     * Regla: solo se puede ejecutar el último día del mes actual.
     */
    public void setShiftForNextMonth(String email, WorkShift shift) {

        Professional professional = getLoggedProfessional(email);

        LocalDate today = LocalDate.now(CO_ZONE);
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());

        if (!today.equals(lastDay)) {
            throw new RuntimeException("Solo puedes cambiar tu jornada el último día del mes.");
        }

        LocalDate firstNextMonth = today.with(TemporalAdjusters.firstDayOfNextMonth());
        int year = firstNextMonth.getYear();
        int month = firstNextMonth.getMonthValue();

        // Upsert (crear o actualizar)
        ProfessionalMonthlyShift cfg = monthlyShiftRepository
                .findByProfessionalIdAndYearAndMonth(professional.getId(), year, month)
                .orElseGet(ProfessionalMonthlyShift::new);

        cfg.setProfessional(professional);
        cfg.setYear(year);
        cfg.setMonth(month);
        cfg.setShift(shift);

        monthlyShiftRepository.save(cfg);

        // Cancelar citas del mes siguiente que ya no cuadren con el nuevo shift
        cancelConflictingAppointmentsForMonth(professional.getId(), year, month, shift);
    }

    /** Obtiene la jornada configurada para (año, mes). Si no existe, devuelve null. */
    public WorkShift getShiftForMonth(String email, int year, int month) {
        Professional professional = getLoggedProfessional(email);

        return monthlyShiftRepository
                .findByProfessionalIdAndYearAndMonth(professional.getId(), year, month)
                .map(ProfessionalMonthlyShift::getShift)
                .orElse(null);
    }

    private void cancelConflictingAppointmentsForMonth(Long professionalId, int year, int month, WorkShift newShift) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDateExclusive = startDate.plusMonths(1);

        OffsetDateTime start = startDate.atStartOfDay(CO_ZONE).toOffsetDateTime();
        OffsetDateTime end = endDateExclusive.atStartOfDay(CO_ZONE).toOffsetDateTime();

        List<Appointment> appts = appointmentRepository
                .findByProfessionalIdAndStartAtBetweenAndStatusIn(
                        professionalId, start, end, List.of(AppointmentStatus.BOOKED)
                );

        List<LocalTime> allowed = slotsByShift(newShift);
        OffsetDateTime now = OffsetDateTime.now(CO_ZONE);

        for (Appointment a : appts) {
            // Convertimos a hora local Colombia
            LocalTime apptHour = a.getStartAt()
                    .atZoneSameInstant(CO_ZONE)
                    .toLocalTime()
                    .withMinute(0).withSecond(0).withNano(0);

            if (!allowed.contains(apptHour)) {
                a.setStatus(AppointmentStatus.CANCELLED);
                a.setCancelledAt(now);
            }
        }

        appointmentRepository.saveAll(appts);
    }

    /** Slots permitidos por jornada (a las "en punto") */
    private List<LocalTime> slotsByShift(WorkShift shift) {
        List<LocalTime> slots = new ArrayList<>();

        if (shift == WorkShift.MORNING || shift == WorkShift.FULL) {
            slots.add(LocalTime.of(7, 0));
            slots.add(LocalTime.of(8, 0));
            slots.add(LocalTime.of(9, 0));
            slots.add(LocalTime.of(10, 0));
            slots.add(LocalTime.of(11, 0));
        }

        if (shift == WorkShift.AFTERNOON || shift == WorkShift.FULL) {
            slots.add(LocalTime.of(13, 0));
            slots.add(LocalTime.of(14, 0));
            slots.add(LocalTime.of(15, 0));
            slots.add(LocalTime.of(16, 0));
            slots.add(LocalTime.of(17, 0));
        }

        return slots;
    }
}