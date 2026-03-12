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
import java.util.List;
import java.util.stream.Collectors;

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
    public Venta registrarVenta(VentaRequestDTO request) {
        Venta venta = new Venta();

        // 1. Buscar Cliente
        Cliente cliente = clienteRepo.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        venta.setCliente(cliente);

        // 2. Lógica de montos (Boleta/Factura)
        BigDecimal subtotal = request.getSubtotal();
        BigDecimal igv = BigDecimal.ZERO;
        if ("FACTURA".equalsIgnoreCase(request.getTipoComprobante())) {
            igv = subtotal.multiply(new BigDecimal("0.18"));
            venta.setTotal(subtotal.add(igv));
        } else {
            venta.setTotal(subtotal);
        }
        venta.setIgv(igv);
        venta.setTipoComprobante(TipoComprobante.valueOf(request.getTipoComprobante()).toString().toUpperCase());
        venta.setNroDocumento(request.getNroDocumento());
        venta.setMetodoPago(MetodoPago.valueOf(request.getMetodoPago().toString().toUpperCase()).toString().toUpperCase());

        // 3. MAPEAR DETALLES (Aquí estaba el error)
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO dDto : request.getDetalles()) {
            DetalleVenta dv = new DetalleVenta();

            Producto producto = productoRepo.findById(dDto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dDto.getIdProducto()));

            if (producto.getStock() < dDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            dv.setProducto(producto);
            dv.setCantidad(dDto.getCantidad());
            dv.setPrecioUnitario(dDto.getPrecioUnitario());
            dv.setSubtotal(dDto.getPrecioUnitario().multiply(new BigDecimal(dDto.getCantidad())));
            dv.setVenta(venta);

            // Descontar stock
            producto.setStock(producto.getStock() - dDto.getCantidad());
            productoRepo.save(producto);

            detalles.add(dv);
        }

        venta.setDetalles(detalles);
        return ventaRepo.save(venta);
    }

    public List<Venta> listarVentasPorCliente(Long idCliente) {
        return  ventaRepo.findByCliente_IdClienteOrderByIdVentaDesc(idCliente);
    }

    public List<DetalleVentaDTO> listarDetallesPorVenta(Integer idVenta) {

        List<DetalleVenta> lista = detalleRepo.findByVenta_IdVenta(idVenta);


        return lista.stream().map(d -> {
            DetalleVentaDTO dto = new DetalleVentaDTO();
            dto.setIdProducto(d.getProducto().getIdProducto());
            dto.setNombreProducto(d.getProducto().getNombre());
            dto.setCantidad(d.getCantidad());
            dto.setPrecioUnitario(d.getPrecioUnitario());
            dto.setSubtotal(d.getSubtotal());
            return dto;
        }).collect(Collectors.toList());
    }


    @Transactional
    public void anularVenta(Long idVenta) {
        Venta venta = ventaRepo.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));

        if ("ANULADA".equalsIgnoreCase(venta.getEstado().toString())) {
            throw new RuntimeException("La venta ya está anulada.");
        }
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            int cantidadComprada = detalle.getCantidad();

            producto.setStock(producto.getStock() + cantidadComprada);

            if (producto.getStock() > 0) {
                producto.setCondicion(Condicion.En_stock);
            }

            productoRepo.save(producto);
        }
        venta.setEstado(EstadoVenta.Anulada.toString().toUpperCase());
        ventaRepo.save(venta);
    }

    @Transactional
    public Venta getVenta(Long idVenta) {
        return ventaRepo.findById(idVenta).orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));
    }

}