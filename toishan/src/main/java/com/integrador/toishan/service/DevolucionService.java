package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.DetalleDevolucionCreateDTO;
import com.integrador.toishan.dto.createDTO.DevolucionCreateDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.DevolucionRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.VentaRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class DevolucionService {

    @Autowired
    private DevolucionRepo devolucionRepo;

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    public Devolucion crearDevolucion(DevolucionCreateDTO dto) {

        Venta venta = ventaRepo.findById(dto.getIdVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Devolucion dev = new Devolucion();
        dev.setVenta(venta);
        dev.setEstado(EstadoDevolucion.Registrada);

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleDevolucionCreateDTO det : dto.getDetalles()) {

            Producto p = productoRepo.findById(det.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleDevolucion dd = new DetalleDevolucion();
            dd.setDevolucion(dev);
            dd.setProducto(p);
            dd.setCantidad(det.getCantidad());
            dd.setPrecioUnitario(p.getPrecio());
            dd.setSubtotal(
                    p.getPrecio().multiply(BigDecimal.valueOf(det.getCantidad()))
            );

            p.setStock(p.getStock() + det.getCantidad());

            dev.getDetalles().add(dd);
            total = total.add(dd.getSubtotal());
        }

        dev.setTotalDevuelto(total);
        return devolucionRepo.save(dev);
    }

    public void anularDevolucion(Long id) {

        Devolucion d = devolucionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Devolución no encontrada"));

        d.setEstado(EstadoDevolucion.Anulada);

        for (DetalleDevolucion det : d.getDetalles()) {
            Producto p = det.getProducto();
            p.setStock(p.getStock() - det.getCantidad());
        }
    }
}
