package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoVenta estado;
enum EstadoVenta
{
    Registrada, Anulada
}
    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleVenta> detalles;

    @OneToMany(mappedBy = "venta")
    private List<Devolucion> devoluciones;

    public Venta(Long idVenta, Cliente cliente, LocalDateTime fechaVenta, BigDecimal total, EstadoVenta estado, List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = EstadoVenta.Registrada;
        this.detalles = detalles;
    }

    public Venta() {
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
