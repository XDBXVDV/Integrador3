package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.CategoriaDTO;
import com.integrador.toishan.model.Categoria;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.repo.CategoriaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepo categoriaRepo;

    public Categoria crear(CategoriaDTO dto) {
        Categoria c = new Categoria();
        c.setNombre(dto.getNombre());
        c.setEstado(Estado.Activo);
        return categoriaRepo.save(c);
    }

    public void desactivar(Long id) {
        Categoria c = categoriaRepo.findById(id).orElseThrow();
        c.setEstado(Estado.Inactivo);
        categoriaRepo.save(c);
    }

    public void activar(Long id) {
        Categoria c = categoriaRepo.findById(id).orElseThrow();
        c.setEstado(Estado.Activo);
        categoriaRepo.save(c);
    }
}

