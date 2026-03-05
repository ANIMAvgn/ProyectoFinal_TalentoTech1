package com.prisma.psicologia.service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.ProfessionalListItemResponse;
import com.prisma.psicologia.dto.SlotsResponse;
import com.prisma.psicologia.model.*;
import com.prisma.psicologia.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientAvailabilityService {

    private static final ZoneId CO_ZONE = ZoneId.of("America/Bogota");

    private final ProfessionalMonthlyShiftRepository monthlyShiftRepository;
    private final ProfessionalRepository professionalRepository;
    private final AppointmentRepository appointmentRepository;

    /** ✅ Regla: solo se permite agendar/consultar este mes o el siguiente (Colombia) */
    private void validateAllowedMonth(LocalDate date) {
        LocalDate today = LocalDate.now(CO_ZONE);

        YearMonth allowed1 = YearMonth.from(today);
        YearMonth allowed2 = YearMonth.from(today.plusMonths(1));
        YearMonth target = YearMonth.from(date);

        if (!target.equals(allowed1) && !target.equals(allowed2)) {
            throw new RuntimeException("Solo puedes escoger citas para el mes actual o el mes siguiente");
        }
    }

    /** ✅ Profesionales disponibles para esa fecha (solo si tienen shift configurado para ese mes) */
    public List<ProfessionalListItemResponse> listProfessionalsForDate(LocalDate date) {
        validateAllowedMonth(date);

        int year = date.getYear();
        int month = date.getMonthValue();

        // turnos configurados para el mes
        List<ProfessionalMonthlyShift> shifts = monthlyShiftRepository.findByYearAndMonth(year, month);

        if (shifts.isEmpty()) return List.of();

        // traer profesionales por ids
        List<Long> profIds = shifts.stream()
                .map(s -> s.getProfessional().getId())
                .distinct()
                .toList();

        // Nota: findAllById no garantiza orden; está bien para lista
        Map<Long, WorkShift> shiftByProfId = shifts.stream()
                .collect(Collectors.toMap(
                        s -> s.getProfessional().getId(),
                        ProfessionalMonthlyShift::getShift,
                        (a, b) -> a
                ));

        List<Professional> professionals = professionalRepository.findAllById(profIds);

        List<ProfessionalListItemResponse> response = new ArrayList<>();
        for (Professional p : professionals) {
            User u = p.getUser();
            WorkShift ws = shiftByProfId.get(p.getId());
            response.add(new ProfessionalListItemResponse(
                    p.getId(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getEmail(),
                    ws == null ? null : ws.name()
            ));
        }
        return response;
    }

    /** ✅ Slots disponibles para un profesional y una fecha */
    public SlotsResponse getSlotsForProfessional(LocalDate date, Long professionalId) {
        validateAllowedMonth(date);

        // Solo lunes a viernes
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return new SlotsResponse(professionalId, date, CO_ZONE.getId(), null, List.of());
        }

        int year = date.getYear();
        int month = date.getMonthValue();

        WorkShift shift = monthlyShiftRepository
                .findByProfessionalIdAndYearAndMonth(professionalId, year, month)
                .map(ProfessionalMonthlyShift::getShift)
                .orElseThrow(() -> new RuntimeException("Este profesional no tiene horario configurado para ese mes"));

        // slots base por jornada
        List<LocalTime> baseSlots = slotsByShift(shift);

        // obtener citas BOOKED del día para marcar ocupados
        OffsetDateTime startDay = date.atStartOfDay(CO_ZONE).toOffsetDateTime();
        OffsetDateTime endDay = date.plusDays(1).atStartOfDay(CO_ZONE).toOffsetDateTime();

        List<Appointment> booked = appointmentRepository.findByProfessionalIdAndStartAtBetweenAndStatus(
                professionalId, startDay, endDay, AppointmentStatus.BOOKED
        );

        Set<LocalTime> takenTimes = booked.stream()
                .map(a -> a.getStartAt().atZoneSameInstant(CO_ZONE).toLocalTime().withMinute(0).withSecond(0).withNano(0))
                .collect(Collectors.toSet());

        List<String> available = baseSlots.stream()
                .filter(t -> !takenTimes.contains(t))
                .map(LocalTime::toString) // "07:00"
                .toList();

        return new SlotsResponse(professionalId, date, CO_ZONE.getId(), shift.name(), available);
    }

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