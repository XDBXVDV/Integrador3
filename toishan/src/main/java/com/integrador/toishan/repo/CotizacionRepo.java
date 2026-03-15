package com.integrador.toishan.repo;

import com.integrador.toishan.model.Cotizacion;
import com.integrador.toishan.model.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CotizacionRepo extends JpaRepository<Cotizacion,Long> {
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
}
