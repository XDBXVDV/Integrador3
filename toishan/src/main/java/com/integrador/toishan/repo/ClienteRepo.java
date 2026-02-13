package com.integrador.toishan.repo;

import com.integrador.toishan.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente,Long> {
}
