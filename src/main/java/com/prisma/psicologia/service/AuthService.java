package com.prisma.psicologia.service;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prisma.psicologia.dto.LoginRequest;
import com.prisma.psicologia.dto.LoginResponse;
import com.prisma.psicologia.dto.ResetPasswordRequest;
import com.prisma.psicologia.model.PasswordResetToken;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.repository.PasswordResetTokenRepository;
import com.prisma.psicologia.repository.PatientRepository;
import com.prisma.psicologia.repository.ProfessionalRepository;
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ProfessionalRepository professionalRepository;

    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    public LoginResponse login(LoginRequest request) {

        String emailNorm = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmailWithRole(emailNorm)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);

        String roleName = user.getRole().getName();

        Long patientId = null;
        Long professionalId = null;

        if ("PATIENT".equals(roleName)) {
            patientId = patientRepository.findByUserId(user.getId())
                    .map(p -> p.getId())
                    .orElse(null);
        } else if ("PROFESSIONAL".equals(roleName)) {
            professionalId = professionalRepository.findByUserId(user.getId())
                    .map(p -> p.getId())
                    .orElse(null);
        }

        return new LoginResponse(
                token,
                roleName,
                user.getId(),
                patientId,
                professionalId,
                user.getNombre(),
                user.getApellido()
        );
    }

    public void sendResetCode(String email) {

        String emailNorm = email == null ? null : email.trim().toLowerCase();

        User user = userRepository.findByEmail(emailNorm)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String codigo = String.valueOf((int) (Math.random() * 900000) + 100000);

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setCodigo(codigo);
        token.setExpiracion(LocalDateTime.now().plusMinutes(15));
        token.setUsado(false);

        tokenRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailNorm);
        message.setSubject("Recuperación de contraseña");
        message.setText("Tu código es: " + codigo);

        mailSender.send(message);
    }

    public void resetPassword(ResetPasswordRequest request) {

        String emailNorm = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(emailNorm)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PasswordResetToken token = tokenRepository
                .findTopByUserAndCodigoAndUsadoFalseOrderByExpiracionDesc(user, request.getCodigo())
                .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (token.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNuevaPassword()));
        token.setUsado(true);

        userRepository.save(user);
        tokenRepository.save(token);
    }
}