package com.integrador.toishan.repo;

import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProveedorRepo extends JpaRepository<Proveedor,Long> {
    boolean existsByRuc(String ruc);
  List<Proveedor>  findByEstado(Estado estado);
}
