package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.CotizacionDTO;
import com.integrador.toishan.model.*;
import com.integrador.toishan.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CotizacionService {

    @Autowired
    private CotizacionRepo cotizacionRepo;
    @Autowired
    private PedidoCompraRepo pedidoRepo;
    @Autowired
    private ProductoRepo productoRepo;
    @Autowired
    private ProveedorRepo proveedorRepo;
    @Autowired
    private OrdenRepo ordenRepo;

    @Transactional
    public Cotizacion guardarCotizacion(CotizacionDTO dto) {
        // 1. Validaciones iniciales
        PedidoCompra pedido = pedidoRepo.findById(dto.getIdPedidoCompra())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Proveedor prov = proveedorRepo.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // 2. Crear la cabecera
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setPedidoCompra(pedido);
        cotizacion.setProveedor(prov);
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);

        BigDecimal totalGeneral = BigDecimal.ZERO;
        List<DetalleCotizacion> detalles = new ArrayList<>();

        // 3. Procesar los items del DTO
        for (var item : dto.getItems()) {
            Producto prod = productoRepo.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getIdProducto()));

            DetalleCotizacion det = new DetalleCotizacion();
            det.setCotizacion(cotizacion);
            det.setProducto(prod);
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitarioOfertado(item.getPrecioUnitario());

            BigDecimal subtotal = item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad()));
            det.setSubtotal(subtotal);

            totalGeneral = totalGeneral.add(subtotal);
            detalles.add(det);
        }

        cotizacion.setDetalles(detalles);
        cotizacion.setTotalCotizado(totalGeneral);

        // 4. Cambiar el estado del pedido original para que no se cotice dos veces
        pedido.setEstado(EstadoPedidoCompra.COTIZADO);
        pedidoRepo.save(pedido);

        return cotizacionRepo.save(cotizacion);
    }

    @Transactional
    public void aprobarCotizacion(Long idCotizacion) {
        Cotizacion cot = cotizacionRepo.findById(idCotizacion)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // 1. Cambiar estado de la cotización
        cot.setEstado(EstadoCotizacion.ACEPTADA);
        cotizacionRepo.save(cot);

        // 2. Generar la Orden de Compra
        OrdenCompra orden = new OrdenCompra();
        orden.setCotizacion(cot);
        orden.setTotal(cot.getTotalCotizado());
        orden.setEstado(EstadoOrden.EMITIDA);
        orden.setNroOrden("OC-" + System.currentTimeMillis());

        ordenRepo.save(orden);
    }

    @Transactional
    public void rechazarCotizacion(Long idCotizacion) {
        Cotizacion cot = cotizacionRepo.findById(idCotizacion).orElseThrow();
        cot.setEstado(EstadoCotizacion.RECHAZADA);
        cotizacionRepo.save(cot);
    }

    public List<Cotizacion> listarPendientes() {
        return cotizacionRepo.findByEstado(EstadoCotizacion.PENDIENTE);
    }
}