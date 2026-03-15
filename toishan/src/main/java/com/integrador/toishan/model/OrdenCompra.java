package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrdenCompra;

    @OneToOne
    @JoinColumn(name = "id_cotizacion")
    private Cotizacion cotizacion;

    private String nroOrden;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(insertable = false, updatable = false)
    private LocalDateTime fechaEmision;

    public OrdenCompra() {}

    public OrdenCompra(Long idOrdenCompra, Cotizacion cotizacion, String nroOrden, BigDecimal total, EstadoOrden estado, LocalDateTime fechaEmision) {
        this.idOrdenCompra = idOrdenCompra;
        this.cotizacion = cotizacion;
        this.nroOrden = nroOrden;
        this.total = total;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
    }

    public Long getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Long idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public String getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(String nroOrden) {
        this.nroOrden = nroOrden;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}