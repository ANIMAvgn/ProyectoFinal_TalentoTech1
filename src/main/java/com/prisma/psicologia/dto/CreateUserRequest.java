package com.prisma.psicologia.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "CC|TI", message = "tipoDocumento debe ser CC o TI")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30)
    private String numeroDocumento;

    @NotBlank(message = "El celular es obligatorio")
    @Size(max = 20)
    private String celular;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Size(max = 150, message = "El email es demasiado largo")
    private String email;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150)
    private String direccion;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "PATIENT|PROFESSIONAL", message = "role debe ser PATIENT o PROFESSIONAL")
    private String role;
}


// http://localhost:8081/admin/users

// {
//   "nombre": "Juan",
//   "apellido": "Osorio",
//   "tipoDocumento": "CC",
//   "numeroDocumento": "1000001010",
//   "celular": "3001112233",
//   "email": "juan.osorio@correo.com",
//   "ciudad": "Medellin",
//   "direccion": "Calle 10 #20-30",
//   "password": "123456",
//   "role": "PROFESSIONAL"
// }