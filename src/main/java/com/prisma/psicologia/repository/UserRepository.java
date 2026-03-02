package com.prisma.psicologia.repository;

import com.prisma.psicologia.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNumeroDocumento(String numeroDocumento);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNumeroDocumento(String numeroDocumento);

    // ✅ Para login: trae el rol en la misma consulta y evita cargas pesadas
    @Query("select u from User u join fetch u.role where u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);
}


  


