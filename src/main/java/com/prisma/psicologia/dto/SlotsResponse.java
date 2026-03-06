package com.prisma.psicologia.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlotsResponse {
    private Long professionalId;
    private LocalDate date;
    private String timezone; // America/Bogota
    private String shift;    // MORNING/AFTERNOON/FULL
    private List<String> availableSlots; // ["07:00","08:00",...]
}

// “¿Qué horas están disponibles para agendar con este profesional el día X?”

// /patient/professionals/{id}/slots

// http://localhost:8081/patient/professionals/1/slots?date=2026-03-10

// Authorization: Bearer TOKEN_DEL_PACIENTE