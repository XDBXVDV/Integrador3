package com.integrador.toishan.repo;

import com.integrador.toishan.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepo extends JpaRepository<Categoria, Long> {
}
