package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;

    private Long userId;
    private Long patientId;       // null si no es PATIENT
    private Long professionalId;  // null si no es PROFESSIONAL

    private String nombre;
    private String apellido;
}