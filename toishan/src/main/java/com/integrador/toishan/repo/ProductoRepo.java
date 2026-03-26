package com.integrador.toishan.repo;

import com.integrador.toishan.model.Condicion;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto,Long> {
    @Query("SELECT p FROM Producto p WHERE p.condicion = 'Agotado' OR p.condicion = 'Stock_bajo'")
    List<Producto> findProductosFaltantes();

    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:idCat IS NULL OR p.categoria.idCategoria = :idCat) AND " +
            "(:idMar IS NULL OR p.marca.idMarca = :idMar) AND " +
            "(:est IS NULL OR p.estado = :est)")
    List<Producto> filtrarInventario(
            @Param("nombre") String nombre,
            @Param("idCat") Long idCat,
            @Param("idMar") Long idMar,
            @Param("est") Estado est
    );


    long countByCondicionIn(List<Condicion> condiciones);
}
