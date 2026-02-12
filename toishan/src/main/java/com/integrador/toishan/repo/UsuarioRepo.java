package com.integrador.toishan.repo;

import com.integrador.toishan.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepo extends JpaRepository<Usuario,Long> {
}
