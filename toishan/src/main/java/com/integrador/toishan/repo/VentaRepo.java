package com.integrador.toishan.repo;

import com.integrador.toishan.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepo extends JpaRepository<Venta,Long> {
    List<Venta> findByCliente_IdClienteOrderByIdVentaDesc(Long idCliente);
}
