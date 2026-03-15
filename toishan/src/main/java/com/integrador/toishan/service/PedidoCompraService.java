package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.RegistroPedidoDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.PedidoCompraRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.ProveedorRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PedidoCompraService {

    @Autowired private PedidoCompraRepo pedidoRepo;
    @Autowired private ProductoRepo productoRepo;
    @Autowired private EmpleadoRepo empleadoRepo;

    // Lista todos para la tabla de gestión
    public List<PedidoCompra> listarTodos() {
        return pedidoRepo.findAll();
    }

    // Lista solo los que están listos para ser cotizados
    public List<PedidoCompra> listarPedidosPendientes() {
        return pedidoRepo.findByEstado(EstadoPedidoCompra.PENDIENTE);
    }

    public PedidoCompra buscarPorId(Long id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Requerimiento #" + id + " no encontrado"));
    }

    @Transactional
    public PedidoCompra registrarRequerimiento(RegistroPedidoDTO dto) {
        PedidoCompra pedido = new PedidoCompra();

        // Buscamos al empleado que hace la solicitud
        Empleado emp = empleadoRepo.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));

        pedido.setEmpleado(emp);
        pedido.setEstado(EstadoPedidoCompra.PENDIENTE); // Estado inicial
        pedido.setTotal(BigDecimal.ZERO); // Se definirá en la factura/cotización
        pedido.setFecha(LocalDateTime.now());

        List<DetallePedidoCompra> detalles = new ArrayList<>();

        for (var item : dto.getItems()) {
            Producto p = productoRepo.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getIdProducto()));

            DetallePedidoCompra detalle = new DetallePedidoCompra();
            detalle.setPedidoCompra(pedido);
            detalle.setProducto(p);
            detalle.setCantidad(item.getCantidad());

            // Inicializamos en cero ya que es solo un requerimiento de cantidades
            detalle.setPrecioUnitario(BigDecimal.ZERO);
            detalle.setSubtotal(BigDecimal.ZERO);

            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);
        return pedidoRepo.save(pedido);
    }

    @Transactional
    public void actualizarEstado(Long id, EstadoPedidoCompra nuevoEstado) {
        PedidoCompra pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        pedidoRepo.save(pedido);
    }
}