package com.integrador.toishan.repo;

import com.integrador.toishan.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto,Long> {
    @Query("SELECT p FROM Producto p WHERE p.condicion = 'Agotado' OR p.condicion = 'Stock_bajo'")
    List<Producto> findProductosFaltantes();
}
