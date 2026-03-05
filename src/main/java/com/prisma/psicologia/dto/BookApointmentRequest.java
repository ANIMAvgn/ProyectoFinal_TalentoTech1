package com.prisma.psicologia.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookApointmentRequest {

    @NotNull(message = "professionalId es obligatorio")
    private Long professionalId;

    // Fecha y hora en formato ISO con offset, ejemplo: "2026-03-10T07:00:00-05:00"
    @NotNull(message = "startAt es obligatorio")
    private String startAt;
}
