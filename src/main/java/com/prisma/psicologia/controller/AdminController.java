package com.prisma.psicologia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.prisma.psicologia.dto.CreateUserRequest;
import com.prisma.psicologia.dto.UserResponse;
import com.prisma.psicologia.service.AdminService;

import jakarta.validation.Valid; // ✅ ESTE IMPORT

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) { // ✅ AGREGA @Valid

        return ResponseEntity.ok(adminService.createUser(request));
    }

    @GetMapping("/generate-hash")
    public String generateHash(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/whoami")
    public String whoami(Authentication auth) {
        return auth == null ? "AUTH=NULL" : auth.getName() + " " + auth.getAuthorities();
    }
}