package com.prisma.psicologia.dto;

import lombok.*;

@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String codigo;
    private String nuevaPassword;
}
