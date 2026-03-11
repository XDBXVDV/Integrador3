package com.integrador.toishan.service;


import com.integrador.toishan.dto.createDTO.DetalleVentaDTO;
import com.integrador.toishan.dto.createDTO.VentaRequestDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class VentaService {

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private DetalleVentaRepo detalleRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private ClienteRepo clienteRepo;

    @Transactional
    public Venta registrarVenta(VentaRequestDTO dto) {
        // 1. Buscar al cliente
        Cliente cliente = clienteRepo.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        // 2. Crear y configurar la cabecera de la Venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setTotal(dto.getTotal());
        venta.setEstado(EstadoVenta.Registrada);

        // Convertir el String del DTO al Enum de Java
        try {
            venta.setMetodoPago(MetodoPago.valueOf(dto.getMetodoPago().toUpperCase()));
        } catch (IllegalArgumentException e) {
            venta.setMetodoPago(MetodoPago.TARJETA); // Valor por defecto si hay error
        }

        // Guardamos la venta primero para generar el ID
        Venta ventaGuardada = ventaRepo.save(venta);

        // 3. Procesar cada detalle y actualizar Stock
        for (DetalleVentaDTO item : dto.getDetalles()) {
            Producto producto = productoRepo.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getIdProducto()));

            // VALIDACIÓN DE STOCK REAL EN BD
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() +
                        " (Disponible: " + producto.getStock() + ")");
            }

            // Descontar stock y actualizar condición
            producto.setStock(producto.getStock() - item.getCantidad());

            if (producto.getStock() == 0) {
                producto.setCondicion(Condicion.Agotado);
            } else if (producto.getStock() <= producto.getStockMinimo()) {
                producto.setCondicion(Condicion.Stock_bajo);
            }

            productoRepo.save(producto);

            // Crear el objeto detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getSubtotal());

            detalleRepo.save(detalle);
        }

        return ventaGuardada;
    }
}