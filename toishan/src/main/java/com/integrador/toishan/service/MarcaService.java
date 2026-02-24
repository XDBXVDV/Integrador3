package com.integrador.toishan.service;

import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Marca;
import com.integrador.toishan.repo.MarcaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


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

        public Marca editar(Long id, Marca marca) {
            return marcaRepo.findById(id).map(marca1 ->  {
                marca1.setNombre(marca.getNombre());
                return marcaRepo.save(marca1);
            }).orElseThrow(() -> new RuntimeException("Marca no existe"));
        }

        public List<Marca> findActivos() {
            return marcaRepo.findByEstado(Estado.Activo);
        }

    }


