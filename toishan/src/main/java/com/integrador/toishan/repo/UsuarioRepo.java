package com.integrador.toishan.repo;

import com.integrador.toishan.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario,Long> {
    boolean existsByUsuario(String usuario);
    Optional<Usuario> findByUsuario(String usuario);
    boolean existsByEmail(String email);
}
