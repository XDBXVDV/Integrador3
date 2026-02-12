package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Long idDevolucion;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Column(name = "total_devuelto", nullable = false)
    private BigDecimal totalDevuelto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoDevolucion estado;
    public enum EstadoDevolucion {
        Registrada,
        Aunlada
    }

    @OneToMany(
            mappedBy = "devolucion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleDevolucion> detalles ;

    public Devolucion(Long idDevolucion, Venta venta, LocalDateTime fechaDevolucion, BigDecimal totalDevuelto, EstadoDevolucion estado, List<DetalleDevolucion> detalles) {
        this.idDevolucion = idDevolucion;
        this.venta = venta;
        this.fechaDevolucion = fechaDevolucion;
        this.totalDevuelto = totalDevuelto;
        this.estado = EstadoDevolucion.Registrada;
        this.detalles = detalles;
    }
    public Devolucion() {

    }

    public Long getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(Long idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public BigDecimal getTotalDevuelto() {
        return totalDevuelto;
    }

    public void setTotalDevuelto(BigDecimal totalDevuelto) {
        this.totalDevuelto = totalDevuelto;
    }

    public EstadoDevolucion getEstado() {
        return estado;
    }

    public void setEstado(EstadoDevolucion estado) {
        this.estado = estado;
    }

    public List<DetalleDevolucion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucion> detalles) {
        this.detalles = detalles;
    }
}

