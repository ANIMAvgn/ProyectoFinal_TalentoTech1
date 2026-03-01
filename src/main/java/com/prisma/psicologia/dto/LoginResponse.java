package com.prisma.psicologia.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
}
