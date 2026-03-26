package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.RegistroPedidoDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Service
public class PedidoCompraService {

    @Autowired
    private PedidoCompraRepo pedidoRepo;
    @Autowired
    private ProductoRepo productoRepo;
    @Autowired
    private EmpleadoRepo empleadoRepo;
    @Autowired
    private ProveedorRepo proveedorRepo;
    @Autowired
    private RequerimientoRepo requerimientoRepo;
    @Autowired
    private DetallePedidoCompraRepo detalleRepo;

    public List<PedidoCompra> listarTodos() {
        return pedidoRepo.findAll();
    }

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

        Empleado emp = empleadoRepo.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + dto.getIdEmpleado()));

        pedido.setEmpleado(emp);
        pedido.setEstado(EstadoPedidoCompra.PENDIENTE);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setFecha(LocalDateTime.now());

        List<DetallePedidoCompra> detalles = new ArrayList<>();

        for (var item : dto.getItems()) {
            Producto p = productoRepo.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getIdProducto()));

            DetallePedidoCompra detalle = new DetallePedidoCompra();
            detalle.setPedidoCompra(pedido);
            detalle.setProducto(p);
            detalle.setCantidad(item.getCantidad());

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

    @Transactional
    public void convertirRequerimientosAPedido(List<Long> idsRequerimientos, Long idProveedor, Long idEmpleado) {

        if (idsRequerimientos == null || idProveedor == null || idEmpleado == null) {
            throw new RuntimeException("Uno de los IDs principales llegó NULO al servicio");
        }

        PedidoCompra pedido = new PedidoCompra();
        pedido.setEmpleado(empleadoRepo.findById(idEmpleado).get());
        pedido.setProveedor(proveedorRepo.findById(idProveedor).get());
        pedido.setEstado(EstadoPedidoCompra.PENDIENTE);

        PedidoCompra pedidoGuardado = pedidoRepo.save(pedido);

        for (Long idReq : idsRequerimientos) {
            Requerimiento req = requerimientoRepo.findById(idReq).get();

            DetallePedidoCompra detalle = new DetallePedidoCompra();
            detalle.setPedidoCompra(pedidoGuardado);
            detalle.setProducto(req.getProducto());
            detalle.setCantidad(req.getCantidadSugerida());
            detalle.setPrecioUnitario(req.getProducto().getPrecio()); // Precio actual base
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad())));

            detalleRepo.save(detalle);

            req.setEstado(EstadoRequerimiento.ATENDIDO);
            requerimientoRepo.save(req);
        }
    }



}