package com.integrador.toishan.repo;

import com.integrador.toishan.model.EstadoRequerimiento;
import com.integrador.toishan.model.Requerimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequerimientoRepo extends JpaRepository<Requerimiento, Long> {
    List<Requerimiento> findByEstado(EstadoRequerimiento estado);
}
