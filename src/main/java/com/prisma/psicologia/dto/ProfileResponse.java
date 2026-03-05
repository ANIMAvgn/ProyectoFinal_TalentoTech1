package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String message;      // "Bienvenido, Pedro"
    private String role;         // ADMIN / PATIENT / PROFESSIONAL
    private Long userId;
    private Long patientId;      // null si no aplica
    private Long professionalId; // null si no aplica
    private String email;
}