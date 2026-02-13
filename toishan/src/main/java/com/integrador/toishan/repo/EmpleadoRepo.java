package com.integrador.toishan.repo;

import com.integrador.toishan.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepo extends JpaRepository<Empleado,Long> {
}
