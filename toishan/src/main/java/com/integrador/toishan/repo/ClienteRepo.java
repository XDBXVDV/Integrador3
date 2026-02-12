package com.integrador.toishan.repo;

import com.integrador.toishan.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepo extends JpaRepository<Cliente,Long> {
}
