package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.DetalleVentaCreateDTO;
import com.integrador.toishan.dto.createDTO.VentaCreateDTO;
import com.integrador.toishan.model.DetalleVenta;
import com.integrador.toishan.model.EstadoVenta;
import com.integrador.toishan.model.Producto;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.repo.ClienteRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.VentaRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private ClienteRepo clienteRepo;

    public Venta crearVenta(VentaCreateDTO dto) {

        Venta venta = new Venta();
        venta.setCliente(clienteRepo.findById(dto.getIdCliente()).orElseThrow());
        venta.setEstado(EstadoVenta.Registrada);

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaCreateDTO det : dto.getDetalles()) {

            Producto p = productoRepo.findById(det.getIdProducto()).orElseThrow();

            if (p.getStock() < det.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            p.setStock(p.getStock() - det.getCantidad());

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProducto(p);
            dv.setCantidad(det.getCantidad());
            dv.setPrecioUnitario(p.getPrecio());
            dv.setSubtotal(p.getPrecio().multiply(BigDecimal.valueOf(det.getCantidad())));

            venta.getDetalles().add(dv);
            total = total.add(dv.getSubtotal());
        }

        venta.setTotal(total);
        return ventaRepo.save(venta);
    }

    public void anularVenta(Long idVenta) {
        Venta v = ventaRepo.findById(idVenta).orElseThrow();
        v.setEstado(EstadoVenta.Anulada);

        for (DetalleVenta d : v.getDetalles()) {
            Producto p = d.getProducto();
            p.setStock(p.getStock() + d.getCantidad());
        }
    }
}

