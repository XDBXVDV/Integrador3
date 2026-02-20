package com.integrador.toishan.service;


import com.integrador.toishan.model.Proveedor;
import com.integrador.toishan.repo.ProveedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepo proveedorRepo;
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

        return proveedorRepo.save(proveedor);
    }
}
