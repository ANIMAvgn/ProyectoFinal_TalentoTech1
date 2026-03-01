package com.prisma.psicologia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.CreateUserRequest;
import com.prisma.psicologia.dto.UserResponse;
import com.prisma.psicologia.model.*;
import com.prisma.psicologia.repository.UserRepository;
import com.prisma.psicologia.repository.RoleRepository;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;



@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        // 🔹 Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 🔹 Validar número de documento único
        if (userRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // 🔹 Buscar rol
        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol inválido"));

        // 🔹 Crear usuario
        User user = new User();

        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setTipoDocumento(request.getTipoDocumento());
        user.setNumeroDocumento(request.getNumeroDocumento());
        user.setCelular(request.getCelular());
        user.setCiudad(request.getCiudad());
        user.setDireccion(request.getDireccion());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        // 🔹 Crear perfil según rol
        if (role.getName().equals("PATIENT")) {

            Patient patient = new Patient();
            patient.setUser(user);

            // ⚠ IMPORTANTE:
            // Aquí debes asignar un profesional antes de guardar
            // patient.setProfessional(professionalAsignado);

            patientRepository.save(patient);

            user.setPatient(patient);
        }

        if (role.getName().equals("PROFESSIONAL")) {

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