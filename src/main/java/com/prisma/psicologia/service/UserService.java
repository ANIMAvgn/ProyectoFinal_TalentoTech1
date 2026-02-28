package com.prisma.psicologia.service;

import com.prisma.psicologia.*;
import com.prisma.psicologia.dto.LoginRequest;
import com.prisma.psicologia.exception.ResourceNotFoundException;
import com.prisma.psicologia.model.User;
import com.prisma.psicologia.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository,
    PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User crearUsuario(User user){
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User update(long id, User userDetails){
        User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if(userDetails.getNumeroDocumento()!=null &&
        !userDetails.getNumeroDocumento().trim().isEmpty()){
        user.setNumeroDocumento(userDetails.getNumeroDocumento());
        }
          if(userDetails.getEmail()!=null &&
        !userDetails.getEmail().trim().isEmpty()){
          user.setEmail(userDetails.getEmail());
        }
         if(userDetails.getPasswordHash()!=null &&
        !userDetails.getPasswordHash().trim().isEmpty()){
          user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
        }
            
        if(userDetails.getRole()!=null)
            user.setRole(userDetails.getRole());
        
        return userRepository.save(user);
    }

    public String login(LoginRequest request){
        Optional<User> optionalUser = userRepository.findByNumeroDocumento(request.getNumeroDocumento());
        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("usuario no encontrado");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(request.getPasswordHash(),user.getPasswordHash())){
            throw new ResourceNotFoundException("Contraseña incorrecta");
        }
        return "Login correcto";

    }


}