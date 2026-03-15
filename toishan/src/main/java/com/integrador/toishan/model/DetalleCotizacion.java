package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_cotizaciones")
public class DetalleCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleCot;

    @ManyToOne
    @JoinColumn(name = "id_cotizacion")
    @JsonIgnore
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private int cantidad;
    private BigDecimal precioUnitarioOfertado;
    private BigDecimal subtotal;

    public  DetalleCotizacion() {}

    public DetalleCotizacion(Long idDetalleCot, Cotizacion cotizacion, Producto producto, int cantidad, BigDecimal precioUnitarioOfertado, BigDecimal subtotal) {
        this.idDetalleCot = idDetalleCot;
        this.cotizacion = cotizacion;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioOfertado = precioUnitarioOfertado;
        this.subtotal = subtotal;
    }

    public Long getIdDetalleCot() {
        return idDetalleCot;
    }

    public void setIdDetalleCot(Long idDetalleCot) {
        this.idDetalleCot = idDetalleCot;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioOfertado() {
        return precioUnitarioOfertado;
    }

    public void setPrecioUnitarioOfertado(BigDecimal precioUnitarioOfertado) {
        this.precioUnitarioOfertado = precioUnitarioOfertado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}