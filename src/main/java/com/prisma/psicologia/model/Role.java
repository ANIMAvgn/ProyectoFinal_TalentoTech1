package com.prisma.psicologia.model;



import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name; // ADMIN, PROFESSIONAL, PATIENT

}
