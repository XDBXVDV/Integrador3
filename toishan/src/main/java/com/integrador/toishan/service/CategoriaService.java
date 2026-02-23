package com.integrador.toishan.service;

import com.integrador.toishan.model.Categoria;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.repo.CategoriaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepo categoriaRepo;

    public Categoria findById(Long id)
    {
        return categoriaRepo.findById(id).orElse(null);
    }

    public Collection<Categoria> findAll(){
        return categoriaRepo.findAll();
    }

    public Categoria crear(Categoria categoria) {
        Categoria c = new Categoria();
        c.setNombre(categoria.getNombre());
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

