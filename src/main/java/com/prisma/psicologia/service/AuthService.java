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
import com.prisma.psicologia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.prisma.psicologia.repository.PasswordResetTokenRepository;





@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    public LoginResponse login(LoginRequest request) {

  User user = userRepository.findByEmailWithRole(request.getEmail())
    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getRole().getName());
    }

    public void sendResetCode(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String codigo = String.valueOf((int)(Math.random() * 900000) + 100000);

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setCodigo(codigo);
        token.setExpiracion(LocalDateTime.now().plusMinutes(15));
        token.setUsado(false);

        tokenRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Tu código es: " + codigo);

        mailSender.send(message);
    }

    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
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
