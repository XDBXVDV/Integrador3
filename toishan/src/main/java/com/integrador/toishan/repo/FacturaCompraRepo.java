package com.integrador.toishan.repo;

import com.integrador.toishan.model.FacturaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FacturaCompraRepo extends JpaRepository<FacturaCompra, Long> {
    List<FacturaCompra> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT f.ordenCompra.cotizacion.proveedor.razonSocial, SUM(f.totalPagado) " +
            "FROM FacturaCompra f GROUP BY f.ordenCompra.cotizacion.proveedor.razonSocial")
    List<Object[]> obtenerTotalesPorProveedor();
}
