package com.prisma.psicologia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prisma.psicologia.dto.ForgotPasswordRequest;
import com.prisma.psicologia.dto.LoginRequest;
import com.prisma.psicologia.dto.LoginResponse;
import com.prisma.psicologia.dto.ResetPasswordRequest;
import com.prisma.psicologia.service.AuthLogoutService;
import com.prisma.psicologia.service.AuthService;

import lombok.RequiredArgsConstructor;
 import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthLogoutService authLogoutService;

    private final AuthService authService;


   


   

@PostMapping("/login")
public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
}

  

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendResetCode(request.getEmail());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
    }

       @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        authLogoutService.logout(authHeader);
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}
