package com.prisma.psicologia.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prisma.psicologia.dto.ForgotPasswordRequest;
import com.prisma.psicologia.dto.LoginRequest;
import com.prisma.psicologia.dto.LoginResponse;
import com.prisma.psicologia.dto.ResetPasswordRequest;
import com.prisma.psicologia.service.AuthService;

import lombok.RequiredArgsConstructor;
 import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

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
}
