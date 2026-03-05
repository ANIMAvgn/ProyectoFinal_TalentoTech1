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
