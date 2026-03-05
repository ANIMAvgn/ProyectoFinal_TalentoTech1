package com.prisma.psicologia.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.dto.UpdateProfessionalShiftRequest;
import com.prisma.psicologia.model.WorkShift;
import com.prisma.psicologia.service.ProfessionalShiftService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/professional")
@RequiredArgsConstructor
public class ProfessionalShiftController {

    private final ProfessionalShiftService professionalShiftService;

    /**
     * Cambia la jornada para el MES SIGUIENTE.
     * Solo permitido el último día del mes.
     */
    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PutMapping("/shift")
    public void setShiftForNextMonth(Authentication auth,
                                     @Valid @RequestBody UpdateProfessionalShiftRequest request) {
        professionalShiftService.setShiftForNextMonth(auth.getName(), request.getShift());
    }

    /**
     * Consultar la jornada configurada para un mes específico.
     * Si no existe, devuelve null.
     */
    @PreAuthorize("hasRole('PROFESSIONAL')")
    @GetMapping("/shift")
    public WorkShift getShift(Authentication auth,
                              @RequestParam int year,
                              @RequestParam int month) {
        return professionalShiftService.getShiftForMonth(auth.getName(), year, month);
    }
}
