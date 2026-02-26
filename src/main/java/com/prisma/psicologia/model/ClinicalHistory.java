package com.prisma.psicologia.model;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "clinical_history")
public class ClinicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(columnDefinition = "TEXT")
    private String antecedentesMedicos;

    @Column(columnDefinition = "TEXT")
    private String antecedentesPsicologicos;

    @Column(columnDefinition = "TEXT")
    private String medicacionActual;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @OneToMany(mappedBy = "clinicalHistory", fetch = FetchType.LAZY)
    private List<Document> documents;

    public ClinicalHistory(){}
}
