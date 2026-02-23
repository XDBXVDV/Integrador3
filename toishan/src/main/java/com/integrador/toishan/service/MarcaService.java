package com.integrador.toishan.service;

import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Marca;
import com.integrador.toishan.repo.MarcaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
    public class MarcaService {

        @Autowired
        private MarcaRepo marcaRepo;

        public Marca findMarcaById(Long idMarca){
            return marcaRepo.findById(idMarca).orElse(null);
        }

        public Collection<Marca> findAll(){
            return marcaRepo.findAll();
        }

        public Marca crear(Marca marca) {
            Marca m = new Marca();
            m.setNombre(marca.getNombre());
            m.setEstado(Estado.Activo);
            return marcaRepo.save(m);
        }
        public void desactivar(Long id) {
            Marca marca = marcaRepo.findById(id).orElseThrow();
            marca.setEstado(Estado.Inactivo);
            marcaRepo.save(marca);
        }
        public void activar(Long id) {
            Marca marca = marcaRepo.findById(id).orElseThrow();
            marca.setEstado(Estado.Activo);
            marcaRepo.save(marca);
        }
    }


