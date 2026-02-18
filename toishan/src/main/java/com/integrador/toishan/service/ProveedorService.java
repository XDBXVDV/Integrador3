package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.ProveedorCreateDTO;
import com.integrador.toishan.model.Proveedor;
import com.integrador.toishan.repo.ProveedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepo proveedorRepo;
    public Proveedor crearProveedor(ProveedorCreateDTO dto) {

        // Validaciones básicas
        if (dto.getRuc() == null || dto.getRuc().isBlank()) {
            throw new RuntimeException("El RUC es obligatorio");
        }

        if (proveedorRepo.existsByRuc(dto.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con este RUC");
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setRuc(dto.getRuc());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());

        return proveedorRepo.save(proveedor);
    }
}
