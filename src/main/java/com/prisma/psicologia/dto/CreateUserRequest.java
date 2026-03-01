package com.prisma.psicologia.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String nombre;
    private String apellido;

    private String tipoDocumento;
    private String numeroDocumento;

    private String celular;
    private String email;
    private String ciudad;
    private String direccion;

    private String password;

    // "PATIENT" o "PROFESSIONAL"
    private String role;
}
