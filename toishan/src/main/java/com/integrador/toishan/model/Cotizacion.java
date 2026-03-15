package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cotizaciones")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotizacion;

    @ManyToOne
    @JoinColumn(name = "id_pedido_compra")
    private PedidoCompra pedidoCompra;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    private BigDecimal totalCotizado;

    @Enumerated(EnumType.STRING)
    private EstadoCotizacion estado;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL)
    private List<DetalleCotizacion> detalles;

    public Cotizacion() {}

    public Cotizacion(Long idCotizacion, PedidoCompra pedidoCompra, Proveedor proveedor, BigDecimal totalCotizado, EstadoCotizacion estado, List<DetalleCotizacion> detalles) {
        this.idCotizacion = idCotizacion;
        this.pedidoCompra = pedidoCompra;
        this.proveedor = proveedor;
        this.totalCotizado = totalCotizado;
        this.estado = estado;
        this.detalles = detalles;
    }

    public Long getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public PedidoCompra getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getTotalCotizado() {
        return totalCotizado;
    }

    public void setTotalCotizado(BigDecimal totalCotizado) {
        this.totalCotizado = totalCotizado;
    }

    public EstadoCotizacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoCotizacion estado) {
        this.estado = estado;
    }

    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }
}