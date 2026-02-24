package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Long idDevolucion;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

    private String motivo;

    @Column(insertable = false,name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Column(name = "total_devuelto", nullable = false)
    private BigDecimal totalDevuelto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoDevolucion estado;


    @OneToMany(mappedBy = "devolucion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleDevolucion> detalles = new ArrayList<>();

    public Devolucion(Long idDevolucion, Venta venta, LocalDateTime fechaDevolucion, String motivo, EstadoDevolucion estado, BigDecimal totalDevuelto, List<DetalleDevolucion> detalles) {
        this.idDevolucion = idDevolucion;
        this.venta = venta;
        this.fechaDevolucion = fechaDevolucion;
        this.motivo = motivo;
        this.estado = estado;
        this.totalDevuelto = totalDevuelto;
        this.detalles = detalles;
    }

    public Devolucion() {

    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

