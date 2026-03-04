package com.prisma.psicologia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String role;
}