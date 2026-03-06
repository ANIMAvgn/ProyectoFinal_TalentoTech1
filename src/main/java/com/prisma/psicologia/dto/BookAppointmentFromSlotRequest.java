package com.prisma.psicologia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookAppointmentFromSlotRequest {

    @NotNull(message = "professionalId es obligatorio")
    private Long professionalId;

    // "2026-03-10"
    @NotBlank(message = "date es obligatorio (YYYY-MM-DD)")
    private String date;

    // "07:00"
    @NotBlank(message = "time es obligatorio (HH:mm)")
    private String time;
}

// Este DTO BookAppointmentFromSlotRequest sirve para que el paciente reserve una cita usando uno de los slots que el backend le devolvió. Es más simple que enviar startAt completo porque el frontend solo manda fecha + hora.