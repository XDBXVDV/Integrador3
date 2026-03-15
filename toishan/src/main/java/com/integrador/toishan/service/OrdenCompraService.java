package com.integrador.toishan.service;

import com.integrador.toishan.model.EstadoOrden;
import com.integrador.toishan.model.OrdenCompra;
import com.integrador.toishan.repo.OrdenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenRepo ordenRepo;


    public List<OrdenCompra> listarPorEstado(EstadoOrden estado) {
        return ordenRepo.findByEstado(estado);
    }

    public OrdenCompra buscarPorId(Long id) {
        return ordenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));
    }
}
