package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos_compra")
public class PedidoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedidoCompra;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    private EstadoPedidoCompra estado;

    private BigDecimal total;

    @Column(insertable = false, updatable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL)
    private List<DetallePedidoCompra> detalles;

    public PedidoCompra() {}

    public PedidoCompra(Integer idPedidoCompra, Proveedor proveedor, Empleado empleado, EstadoPedidoCompra estado, BigDecimal total, LocalDateTime fecha, List<DetallePedidoCompra> detalles) {
        this.idPedidoCompra = idPedidoCompra;
        this.proveedor = proveedor;
        this.empleado = empleado;
        this.estado = estado;
        this.total = total;
        this.fecha = fecha;
        this.detalles = detalles;
    }

    public Integer getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(Integer idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public EstadoPedidoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedidoCompra estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<DetallePedidoCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoCompra> detalles) {
        this.detalles = detalles;
    }
}