package com.integrador.toishan.repo;

import com.integrador.toishan.model.Guia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GuiaRepo extends JpaRepository<Guia, Long>
{
    @Query("SELECT g FROM Guia g WHERE " +
            "(g.fechaMovimiento BETWEEN :inicio AND :fin) AND " +
            "(:tipo IS NULL OR g.tipoMovimiento = :tipo) " +
            "ORDER BY g.fechaMovimiento DESC")
    List<Guia> reportarMovimientos(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("tipo") String tipo
    );
}
