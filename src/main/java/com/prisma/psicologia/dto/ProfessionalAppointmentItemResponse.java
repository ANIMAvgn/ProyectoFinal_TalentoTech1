package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfessionalAppointmentItemResponse {

    private Long appointmentId;
    private String startAt; // yyyy-MM-dd HH:mm
    private Integer durationMinutes;
    private String status;

    private Long patientId;
    private String patientNombre;
    private String patientApellido;
    private String patientEmail;
}