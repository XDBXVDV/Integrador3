package com.integrador.toishan.repo;

import com.integrador.toishan.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepo extends JpaRepository<DetalleVenta,Long> {
    List<DetalleVenta> findByVenta_IdVenta(Integer idVenta);
}
