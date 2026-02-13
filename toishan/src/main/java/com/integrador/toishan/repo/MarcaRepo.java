package com.integrador.toishan.repo;

import com.integrador.toishan.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepo extends JpaRepository<Marca,Long> {
}
