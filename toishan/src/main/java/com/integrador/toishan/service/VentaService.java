package com.integrador.toishan.service;


import com.integrador.toishan.model.DetalleVenta;
import com.integrador.toishan.model.EstadoVenta;
import com.integrador.toishan.model.Producto;
import com.integrador.toishan.model.Venta;
import com.integrador.toishan.repo.ClienteRepo;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.repo.VentaRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private ClienteRepo clienteRepo;

     @Autowired
     private EmpleadoRepo empleadoRepo;

    @Transactional
    public Venta crearVenta(Venta venta1) {
        if (venta1.getDetalles() == null || venta1.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un detalle");
        }
        Venta venta = new Venta();


        venta.setCliente(clienteRepo.findById(venta1.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        venta.setEmpleado(empleadoRepo.findById(venta1.getEmpleado().getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado")));
        venta.setEstado(EstadoVenta.Registrada);

        if (venta.getDetalles() == null) {
            venta.setDetalles(new ArrayList<>());
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta det : venta1.getDetalles()) {
            // OJO: Asegúrate que el JSON mande el nombre que Java espera (idproducto)
            Producto p = productoRepo.findById(det.getProducto().getIdproducto())
                    .orElseThrow(() -> new RuntimeException("Producto con ID " + det.getProducto().getIdproducto() + " no encontrado"));

            if (p.getStock() < det.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + p.getNombre());
            }


            p.setStock(p.getStock() - det.getCantidad());
            productoRepo.save(p);

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProducto(p);
            dv.setCantidad(det.getCantidad());
            dv.setPrecioUnitario(p.getPrecio());

            BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(det.getCantidad()));
            dv.setSubtotal(subtotal);

            venta.getDetalles().add(dv);
            total = total.add(subtotal);
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

