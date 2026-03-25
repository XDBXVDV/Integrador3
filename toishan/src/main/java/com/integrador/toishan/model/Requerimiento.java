package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requerimientos_reposicion")
public class Requerimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRequerimiento;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_empleado_almacen")
    private Empleado empleadoAlmacen;

    private Integer cantidadSugerida;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    private EstadoRequerimiento estado;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    public Requerimiento() {}

    public Requerimiento(Long idRequerimiento, Producto producto, Empleado empleadoAlmacen, Integer cantidadSugerida, Prioridad prioridad, EstadoRequerimiento estado, LocalDateTime fechaSolicitud) {
        this.idRequerimiento = idRequerimiento;
        this.producto = producto;
        this.empleadoAlmacen = empleadoAlmacen;
        this.cantidadSugerida = cantidadSugerida;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Long getIdRequerimiento() {
        return idRequerimiento;
    }

    public void setIdRequerimiento(Long idRequerimiento) {
        this.idRequerimiento = idRequerimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Empleado getEmpleadoAlmacen() {
        return empleadoAlmacen;
    }

    public void setEmpleadoAlmacen(Empleado empleadoAlmacen) {
        this.empleadoAlmacen = empleadoAlmacen;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Integer getCantidadSugerida() {
        return cantidadSugerida;
    }

    public void setCantidadSugerida(Integer cantidadSugerida) {
        this.cantidadSugerida = cantidadSugerida;
    }

    public EstadoRequerimiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoRequerimiento estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}