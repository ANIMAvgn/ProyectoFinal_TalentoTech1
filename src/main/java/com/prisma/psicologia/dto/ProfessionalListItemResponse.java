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
