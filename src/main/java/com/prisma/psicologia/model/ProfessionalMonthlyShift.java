package com.prisma.psicologia.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "professional_monthly_shift",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_professional_month",
            columnNames = {"professional_id", "year", "month"}
        )
    }
)
public class ProfessionalMonthlyShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Profesional al que pertenece esta configuración
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    // Año al que aplica el horario
    @Column(nullable = false)
    private Integer year;

    // Mes al que aplica el horario (1-12)
    @Column(nullable = false)
    private Integer month;

    // Jornada elegida
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkShift shift;

    // Fecha en que se creó la configuración
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}