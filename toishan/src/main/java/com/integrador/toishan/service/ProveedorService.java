package com.integrador.toishan.service;


import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Proveedor;
import com.integrador.toishan.repo.ProveedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepo proveedorRepo;

    public Proveedor findbyid(Long id){
        return proveedorRepo.findById(id).orElse(null);
    }

    public Collection<Proveedor> findAll(){
        return proveedorRepo.findAll();
    }

    public Proveedor crearProveedor(Proveedor proveedor1) {


        if (proveedor1.getRuc() == null || proveedor1.getRuc().isBlank()) {
            throw new RuntimeException("El RUC es obligatorio");
        }

        if (proveedorRepo.existsByRuc(proveedor1.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con este RUC");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(proveedor1.getRazonSocial());
        proveedor.setRuc(proveedor1.getRuc());
        proveedor.setTelefono(proveedor1.getTelefono());
        proveedor.setDireccion(proveedor1.getDireccion());
        proveedor.setEmail(proveedor1.getEmail());
        proveedor.setEstado(Estado.Activo);
        return proveedorRepo.save(proveedor);
    }

    public void DesactivarProveedor(Long id) {
        Proveedor proveedor = findbyid(id);
        if (proveedor != null) {
            if(proveedor.getEstado().equals(Estado.Inactivo)){
                throw new RuntimeException("El proveedor ya está inactivo");
            }
            proveedor.setEstado(Estado.Inactivo);
            proveedorRepo.save(proveedor);
        } else  throw new RuntimeException("Proveedor no encontrado");
    }

    public void ReactivarProveedor(Long id) {
        Proveedor proveedor = findbyid(id);
        if (proveedor != null) {
            if(proveedor.getEstado().equals(Estado.Activo)){
                throw new RuntimeException("El proveedor ya está activo");
            }
            proveedor.setEstado(Estado.Activo);
            proveedorRepo.save(proveedor);
        }  else  throw new RuntimeException("Proveedor no encontrado");
    }



}
