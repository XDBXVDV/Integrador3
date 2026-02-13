package com.integrador.toishan.repo;

import com.integrador.toishan.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario,Long> {
    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);
}
