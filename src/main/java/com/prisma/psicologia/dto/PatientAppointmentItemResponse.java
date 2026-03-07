package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientAppointmentItemResponse {

    private Long appointmentId;
    private String startAt; // formato claro
    private Integer durationMinutes;
    private String status;

    private Long professionalId;
    private String professionalNombre;
    private String professionalApellido;
    private String professionalEmail;
}