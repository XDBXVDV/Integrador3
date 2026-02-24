package com.integrador.toishan.repo;

import com.integrador.toishan.model.Categoria;
import com.integrador.toishan.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEstado(Estado estado);
}
