package com.prisma.psicologia.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String modalidad;
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY)
    private List<SessionNote> sessionNotes;

    public Appointment(){}
}
