package com.integrador.toishan.repo;

import com.integrador.toishan.model.EstadoOrden;
import com.integrador.toishan.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepo extends JpaRepository<OrdenCompra,Long> {
    List<OrdenCompra> findByEstado(EstadoOrden estado);
}
