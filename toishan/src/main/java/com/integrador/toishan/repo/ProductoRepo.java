package com.integrador.toishan.repo;

import com.integrador.toishan.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepo extends JpaRepository<Producto,Long> {
}
