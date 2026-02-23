package com.integrador.toishan.service;

import com.integrador.toishan.model.PedidoCompra;
import com.integrador.toishan.repo.PedidoCompraRepo;
import com.integrador.toishan.repo.ProveedorRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepo pedidoCompraRepo;

    @Autowired
    private ProveedorRepo proveedorRepo;

    public Collection<PedidoCompra> findAll(){
        return pedidoCompraRepo.findAll();
    }

    public PedidoCompra findById(Long id){
        return pedidoCompraRepo.findById(id).orElse(null);
    }

    public PedidoCompra create(PedidoCompra pedidoCompra){
        if (pedidoCompra.getDetalles()==null){
            throw new RuntimeException("El pedido debe tener al menos un detalle");
        }
        PedidoCompra pedido= new PedidoCompra();
        pedido.setIdPedidoCompra(pedidoCompra.getIdPedidoCompra());
        pedido.setEmpleado(pedidoCompra.getEmpleado());
        pedido.setProveedor(pedidoCompra.getProveedor());
        pedido.setTotal(pedidoCompra.getTotal());
        pedido.setEstado(pedidoCompra.getEstado());
        return  pedidoCompraRepo.save(pedido);
    }
}
