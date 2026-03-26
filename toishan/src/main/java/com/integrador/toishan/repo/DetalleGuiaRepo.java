package com.integrador.toishan.repo;

import com.integrador.toishan.model.DetalleGuia;
import com.integrador.toishan.model.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleGuiaRepo extends JpaRepository<DetalleGuia, Long> {

    @Query("SELECT d FROM DetalleGuia d WHERE d.guia.idGuia = :idGuia")
    List<DetalleGuia> buscarPorGuia(@Param("idGuia") Long idGuia);
}