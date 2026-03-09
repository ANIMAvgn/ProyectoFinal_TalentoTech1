package com.prisma.psicologia.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.dto.BookApointmentRequest;
import com.prisma.psicologia.dto.BookAppointmentFromSlotRequest;
import com.prisma.psicologia.dto.PatientAppointmentItemResponse;
import com.prisma.psicologia.service.PatientAppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient/appointments")
@RequiredArgsConstructor
public class PatientApointmentController {

    private final PatientAppointmentService patientAppointmentService;


    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping
    public List<PatientAppointmentItemResponse> myAppointments(Authentication auth) {
         return patientAppointmentService.getMyAppointments(auth.getName());
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/{id}/cancel")
    public void cancel(Authentication auth, @PathVariable Long id) {
        patientAppointmentService.cancelAppointment(auth.getName(), id);
    }

    //Reservar cita
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/book")
    public void book(Authentication auth, @Valid @RequestBody BookApointmentRequest request) {
        patientAppointmentService.bookAppointment(auth.getName(), request);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/book-from-slot")
    public void bookFromSlot(Authentication auth,
                         @Valid @RequestBody BookAppointmentFromSlotRequest request) {
    patientAppointmentService.bookFromSlot(auth.getName(), request);
}
}
