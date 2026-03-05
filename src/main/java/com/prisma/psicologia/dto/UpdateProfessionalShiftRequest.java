package com.prisma.psicologia.dto;

import com.prisma.psicologia.model.WorkShift;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfessionalShiftRequest {

    @NotNull(message = "El turno es obligatorio")
    private WorkShift shift;

}