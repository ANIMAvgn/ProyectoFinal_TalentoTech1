package com.prisma.psicologia.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.CreateUserRequest;
import com.prisma.psicologia.dto.UserResponse;
import com.prisma.psicologia.model.Patient;
import com.prisma.psicologia.model.Professional;
import com.prisma.psicologia.model.Role;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.RoleRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        // ✅ Normalizaciones
        String emailNorm = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        String roleNameReq = request.getRole() == null ? "" : request.getRole().trim().toUpperCase();
        String tipoDocNorm = request.getTipoDocumento() == null ? null : request.getTipoDocumento().trim().toUpperCase();
        String numeroDocNorm = request.getNumeroDocumento() == null ? null : request.getNumeroDocumento().trim();

        // Validar email único (ya normalizado)
        if (emailNorm != null && userRepository.existsByEmail(emailNorm)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Validar número de documento único (normalizado)
        if (numeroDocNorm != null && userRepository.existsByNumeroDocumento(numeroDocNorm)) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // Buscar rol en BD (debe existir: ADMIN / PATIENT / PROFESSIONAL)
        Role role = roleRepository.findByName(roleNameReq)
                .orElseThrow(() -> new RuntimeException("Rol inválido: " + request.getRole()));

        // Crear usuario
        User user = new User();
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setTipoDocumento(tipoDocNorm);
        user.setNumeroDocumento(numeroDocNorm);
        user.setCelular(request.getCelular());
        user.setCiudad(request.getCiudad());
        user.setDireccion(request.getDireccion());
        user.setEmail(emailNorm);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        // Crear perfil según rol (sin depender de mayúsculas/espacios)
        if ("PATIENT".equals(roleNameReq)) {
            Patient patient = new Patient();
            patient.setUser(user);
            // ✅ NO se asigna profesional aquí (lo elige el paciente después)
            patientRepository.save(patient);
            user.setPatient(patient);

        } else if ("PROFESSIONAL".equals(roleNameReq)) {
            Professional professional = new Professional();
            professional.setUser(user);
            professionalRepository.save(professional);
            user.setProfessional(professional);
        }

        return new UserResponse(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getEmail(),
                role.getName()
        );
    }
}