package com.integrador.toishan.repo;

import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente,Long> {
    Optional<Cliente> findByUsuario(Usuario usuario);
    Cliente findByUsuarioIdUsuario(Long idUsuario);
}
