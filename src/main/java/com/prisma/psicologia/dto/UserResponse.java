package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String role;
}