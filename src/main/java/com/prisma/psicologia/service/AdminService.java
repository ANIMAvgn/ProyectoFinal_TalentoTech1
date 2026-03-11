package com.prisma.psicologia.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.CreateUserRequest;
import com.prisma.psicologia.dto.UserResponse;
import com.prisma.psicologia.model.Patient;
import com.prisma.psicologia.model.Professional;
import com.prisma.psicologia.model.ProfessionalMonthlyShift;
import com.prisma.psicologia.model.Role;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.model.WorkShift;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalMonthlyShiftRepository;
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
    private final ProfessionalMonthlyShiftRepository professionalMonthlyShiftRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {

        String emailNorm = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        String roleNameReq = request.getRole() == null ? "" : request.getRole().trim().toUpperCase();
        String tipoDocNorm = request.getTipoDocumento() == null ? null : request.getTipoDocumento().trim().toUpperCase();
        String numeroDocNorm = request.getNumeroDocumento() == null ? null : request.getNumeroDocumento().trim();

        if (emailNorm != null && userRepository.existsByEmail(emailNorm)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        if (numeroDocNorm != null && userRepository.existsByNumeroDocumento(numeroDocNorm)) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        Role role = roleRepository.findByName(roleNameReq)
                .orElseThrow(() -> new RuntimeException("Rol inválido: " + request.getRole()));

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

        if ("PATIENT".equals(roleNameReq)) {
            Patient patient = new Patient();
            patient.setUser(user);
            patientRepository.save(patient);
            user.setPatient(patient);

        } else if ("PROFESSIONAL".equals(roleNameReq)) {
            Professional professional = new Professional();
            professional.setUser(user);

            professionalRepository.save(professional);
            user.setProfessional(professional);

            createDefaultShiftsForProfessional(professional);
        }

        return new UserResponse(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getEmail(),
                role.getName()
        );
    }

    private void createDefaultShiftsForProfessional(Professional professional) {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 4; i++) {
            LocalDate targetMonth = today.plusMonths(i);

            int year = targetMonth.getYear();
            int month = targetMonth.getMonthValue();

            boolean exists = professionalMonthlyShiftRepository
                    .findByProfessionalIdAndYearAndMonth(professional.getId(), year, month)
                    .isPresent();

            if (!exists) {
                ProfessionalMonthlyShift shift = new ProfessionalMonthlyShift();
                shift.setProfessional(professional);
                shift.setYear(year);
                shift.setMonth(month);
                shift.setShift(WorkShift.MORNING);

                professionalMonthlyShiftRepository.save(shift);
            }
        }
    }
}