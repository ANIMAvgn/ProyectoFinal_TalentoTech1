package com.prisma.psicologia.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.dto.ProfessionalListItemResponse;
import com.prisma.psicologia.dto.SlotsResponse;
import com.prisma.psicologia.service.PatientAvailabilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientAvailabilityController {

    private final PatientAvailabilityService patientAvailabilityService;

    // ✅ Lista profesionales disponibles para esa fecha (solo si tienen shift del mes)
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/professionals")
    public List<ProfessionalListItemResponse> professionals(Authentication auth,
                                                            @RequestParam String date) {
        return patientAvailabilityService.listProfessionalsForDate(LocalDate.parse(date));
    }

    // ✅ Slots disponibles de un profesional para esa fecha
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/professionals/{id}/slots")
    public SlotsResponse slots(Authentication auth,
                               @PathVariable Long id,
                               @RequestParam String date) {
        return patientAvailabilityService.getSlotsForProfessional(LocalDate.parse(date), id);
    }
}