package com.integrador.toishan.service;

import com.integrador.toishan.dto.detalleDto.ItemGuiaDTO;
import com.integrador.toishan.dto.modelDTO.GuiaDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.DetalleGuiaRepo;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.GuiaRepo;
import com.integrador.toishan.repo.ProductoRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuiaAlmacenService {

    @Autowired
    private GuiaRepo guiaRepo;
    @Autowired
    private DetalleGuiaRepo detalleRepo;
    @Autowired
    private ProductoRepo productoRepo;
    @Autowired
    private EmpleadoRepo empleadoRepo;

    @Transactional
    public Guia procesarGuia(GuiaDTO dto) {

        Guia guia = new Guia();
        guia.setNumeroGuia(dto.getNumeroGuia());
        guia.setTipoMovimiento(dto.getTipoMovimiento());
        guia.setMotivo(dto.getMotivo());
        guia.setIdDocumentoReferencia(dto.getIdDocumentoReferencia());

        Empleado emp = empleadoRepo.findById(dto.getIdEmpleadoAlmacen())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        guia.setEmpleadoAlmacen(emp);

        Guia guiaGuardada = guiaRepo.save(guia);

        for (ItemGuiaDTO itemDto : dto.getItems()) {
            Producto producto = productoRepo.findById(itemDto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (dto.getTipoMovimiento().equals("ENTRADA")) {
                producto.setStock(producto.getStock() + itemDto.getCantidad());
            } else {
                if (producto.getStock() < itemDto.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
                }
                producto.setStock(producto.getStock() - itemDto.getCantidad());
            }

            actualizarCondicion(producto);
            productoRepo.save(producto);

            DetalleGuia detalle = new DetalleGuia();
            detalle.setGuia(guiaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(itemDto.getCantidad());
            detalleRepo.save(detalle);
        }
        return guiaGuardada;
    }

    private void actualizarCondicion(Producto p) {
        if (p.getStock() <= 0) p.setCondicion(Condicion.Agotado);
        else if (p.getStock() <= p.getStockMinimo()) p.setCondicion(Condicion.Stock_bajo);
        else p.setCondicion(Condicion.En_stock);
    }

    public List<Guia> obtenerReporte(String fechaInicio, String fechaFin, String tipo) {
       LocalDateTime inicio = LocalDate.parse(fechaInicio).atStartOfDay();
        LocalDateTime fin = LocalDate.parse(fechaFin).atTime(23, 59, 59);
        String tipoFiltro = (tipo != null && !tipo.isEmpty()) ? tipo : null;

        return guiaRepo.reportarMovimientos(inicio, fin, tipoFiltro);
    }
}