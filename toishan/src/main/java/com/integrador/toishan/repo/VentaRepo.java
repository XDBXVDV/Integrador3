package com.integrador.toishan.repo;

import com.integrador.toishan.dto.modelDTO.ReporteDTO;
import com.integrador.toishan.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepo extends JpaRepository<Venta,Long> {
    List<Venta> findByCliente_IdClienteOrderByIdVentaDesc(Long idCliente);

    @Query("SELECT new com.integrador.toishan.dto.modelDTO.ReporteDTO(STR(v.fechaventa), SUM(v.total)) " +
            "FROM Venta v WHERE v.estado != 'ANULADA' " +
            "GROUP BY STR(v.fechaventa) ORDER BY STR(v.fechaventa) ASC")
    List<ReporteDTO> obtenerVentasPorDia();

    @Query("SELECT new com.integrador.toishan.dto.modelDTO.ReporteDTO(dv.producto.nombre, SUM(dv.cantidad * 1.0)) " +
            "FROM DetalleVenta dv WHERE dv.venta.estado != 'ANULADA' " +
            "GROUP BY dv.producto.nombre ORDER BY SUM(dv.cantidad) DESC")
    List<ReporteDTO> obtenerTopProductos();

    @Query("SELECT new com.integrador.toishan.dto.modelDTO.ReporteDTO(STR(v.fechaventa), SUM(v.total)) " +
            "FROM Venta v " +
            "WHERE v.estado != 'ANULADA' " +
            "AND (:inicio IS NULL OR v.fechaventa >= :inicio) " +
            "AND (:fin IS NULL OR v.fechaventa <= :fin) " +
            "GROUP BY STR(v.fechaventa) ORDER BY STR(v.fechaventa) ASC")
    List<ReporteDTO> obtenerVentasPorDiaFiltrado(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
