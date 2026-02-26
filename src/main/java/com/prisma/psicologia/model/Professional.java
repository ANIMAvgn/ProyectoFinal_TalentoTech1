package com.prisma.psicologia.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "professionals")
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private String especialidad;
    private String numeroLicencia;
    private Double fee;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private List<Document> documents;

    public Professional(){}

    // Getters & Setters
}
