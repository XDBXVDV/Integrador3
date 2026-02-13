package com.integrador.toishan.repo;

import com.integrador.toishan.model.DetalleDevolucion;
import com.integrador.toishan.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepo extends JpaRepository<DetalleVenta,Long> {
}
