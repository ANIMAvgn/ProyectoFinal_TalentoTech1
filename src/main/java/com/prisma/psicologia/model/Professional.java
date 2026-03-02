package com.prisma.psicologia.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "professionals")
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name="user_id", nullable=false, unique=true)
   private User user;

    // Un profesional tiene muchos pacientes
    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private List<Patient> patients;

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY)
    private List<Task> tasks;
}