package com.prisma.psicologia.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.service.ProfessionalAppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/professional/appointments")
@RequiredArgsConstructor
public class ProfessionalAppointmentController {

    private final ProfessionalAppointmentService professionalAppointmentService;

    @PreAuthorize("hasRole('PROFESSIONAL')")
    @PostMapping("/{id}/cancel")
    public void cancel(Authentication auth, @PathVariable Long id) {
        professionalAppointmentService.cancelAppointment(auth.getName(), id);
    }
}