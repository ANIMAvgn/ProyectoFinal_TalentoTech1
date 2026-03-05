package com.prisma.psicologia.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.dto.ProfileResponse;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;

    private User getUserFromAuth(Authentication auth) {
        String email = auth.getName(); // en tu filtro se setea authToken con user.getEmail()
        return userRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ProfileResponse adminProfile(Authentication auth) {
        User user = getUserFromAuth(auth);
        return new ProfileResponse(
                "Bienvenido, " + user.getNombre(),
                user.getRole().getName(),
                user.getId(),
                null,
                null,
                user.getEmail()
        );
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient")
    public ProfileResponse patientProfile(Authentication auth) {
        User user = getUserFromAuth(auth);

        Long patientId = patientRepository.findByUserId(user.getId())
                .map(p -> p.getId())
                .orElse(null);

        return new ProfileResponse(
                "Bienvenido, " + user.getNombre(),
                user.getRole().getName(),
                user.getId(),
                patientId,
                null,
                user.getEmail()
        );
    }

    @PreAuthorize("hasRole('PROFESSIONAL')")
    @GetMapping("/professional")
    public ProfileResponse professionalProfile(Authentication auth) {
        User user = getUserFromAuth(auth);

        Long professionalId = professionalRepository.findByUserId(user.getId())
                .map(p -> p.getId())
                .orElse(null);

        return new ProfileResponse(
                "Bienvenido, " + user.getNombre(),
                user.getRole().getName(),
                user.getId(),
                null,
                professionalId,
                user.getEmail()
        );
    }
}