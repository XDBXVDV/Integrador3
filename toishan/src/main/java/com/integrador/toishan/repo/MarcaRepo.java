package com.integrador.toishan.repo;

import com.integrador.toishan.model.Categoria;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaRepo extends JpaRepository<Marca,Long> {
    List<Marca> findByEstado(Estado estado);
}
