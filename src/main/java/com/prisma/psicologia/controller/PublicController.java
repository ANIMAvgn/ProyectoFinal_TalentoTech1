package com.prisma.psicologia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/")
    public String landing() {
        return "Bienvenido a Prisma Psicología. Para ingresar use POST /auth/login";
    }
}
