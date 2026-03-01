package com.prisma.psicologia.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private LocalDateTime expiracion;

    private Boolean usado = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
