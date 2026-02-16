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
import java.util.ArrayList;

@Service
@Transactional
public class DevolucionService {

    @Autowired
    private DevolucionRepo devolucionRepo;

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Transactional
    public Devolucion crearDevolucion(DevolucionCreateDTO dto) {

        Venta venta = ventaRepo.findById(dto.getIdVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Devolucion devolucion = new Devolucion();
        devolucion.setVenta(venta);
        devolucion.setMotivo(dto.getMotivo());
        devolucion.setEstado(EstadoDevolucion.Registrada);
        devolucion.setDetalles(new ArrayList<>());

        BigDecimal totalDevuelto = BigDecimal.ZERO;

        for (DetalleDevolucionCreateDTO det : dto.getDetalles()) {

            Producto producto = productoRepo.findById(det.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // 1️⃣ Buscar detalle original de la venta
            DetalleVenta detalleVenta = venta.getDetalles().stream()
                    .filter(dv -> dv.getProducto().getIdproducto()==producto.getIdproducto())
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("El producto no pertenece a esta venta")
                    );

            int cantidadVendida = detalleVenta.getCantidad();

            // 2️⃣ Calcular lo ya devuelto
            int cantidadDevuelta = cantidadYaDevuelta(venta, producto);

            // 3️⃣ Validar
            if (cantidadDevuelta + det.getCantidad() > cantidadVendida) {
                throw new RuntimeException(
                        "Cantidad a devolver supera lo vendido. " +
                                "Vendida: " + cantidadVendida +
                                ", Ya devuelta: " + cantidadDevuelta
                );
            }

            // 👉 SI PASA, RECIÉN CREAS EL DETALLE
        }

        devolucion.setTotalDevuelto(totalDevuelto);

        return devolucionRepo.save(devolucion);
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
    private int cantidadYaDevuelta(Venta venta, Producto producto) {

        return venta.getDevoluciones().stream()
                .filter(d -> d.getEstado() != EstadoDevolucion.Anulada)
                .flatMap(d -> d.getDetalles().stream())
                .filter(dd -> dd.getProducto().getIdproducto()==producto.getIdproducto())
                .mapToInt(DetalleDevolucion::getCantidad)
                .sum();
    }
}
