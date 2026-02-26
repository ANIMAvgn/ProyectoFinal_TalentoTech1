package com.prisma.psicologia.model;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String genero;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Document> documents;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private ClinicalHistory clinicalHistory;

    public Patient(){}

    // Getters & Setters
}
