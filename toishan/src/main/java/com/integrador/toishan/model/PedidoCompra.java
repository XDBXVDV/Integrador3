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
    @Column(name = "id_pedido_compra")
    private Long idPedidoCompra;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedidoCompra estado;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;
    @JsonIgnore
    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL)
    private List<DetallePedidoCompra> detalles;

    public PedidoCompra(Long idPedidoCompra, Proveedor proveedor, Empleado empleado, EstadoPedidoCompra estado, BigDecimal total, LocalDateTime fecha, List<DetallePedidoCompra> detalles) {
        this.idPedidoCompra = idPedidoCompra;
        this.proveedor = proveedor;
        this.empleado = empleado;
        this.estado = estado;
        this.total = total;
        this.fecha = fecha;
        this.detalles = detalles;
    }
    public PedidoCompra() {}

    public Long getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(Long idPedidoCompra) {
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoPedidoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedidoCompra estado) {
        this.estado = estado;
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
