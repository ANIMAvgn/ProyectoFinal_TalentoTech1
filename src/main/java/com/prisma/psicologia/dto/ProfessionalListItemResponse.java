package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfessionalListItemResponse {
    private Long professionalId;
    private String nombre;
    private String apellido;
    private String email;
    private String shift; // MORNING / AFTERNOON / FULL (para ese mes)
}

// Sirve para mostrar al paciente los profesionales disponibles en una fecha específica.

// http://localhost:8081/patient/professionals?date=2026-03-10

// GET tiene que tener token paciente