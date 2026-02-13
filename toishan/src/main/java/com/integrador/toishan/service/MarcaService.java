package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.MarcaDTO;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Marca;
import com.integrador.toishan.repo.MarcaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


    @Service
    public class MarcaService {

        @Autowired
        private MarcaRepo marcaRepo;

        public Marca crear(MarcaDTO dto) {
            Marca m = new Marca();
            m.setNombre(dto.getNombre());
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


