package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_guia")
public class DetalleGuia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_guia")
    private Long idDetalleGuia;

    @ManyToOne
    @JoinColumn(name = "id_guia")
    @JsonBackReference
    private Guia guia;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Integer cantidad;

    public DetalleGuia() {}

    public DetalleGuia(Long idDetalleGuia, Guia guia, Producto producto, Integer cantidad) {
        this.idDetalleGuia = idDetalleGuia;
        this.guia = guia;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Long getIdDetalleGuia() {
        return idDetalleGuia;
    }

    public void setIdDetalleGuia(Long idDetalleGuia) {
        this.idDetalleGuia = idDetalleGuia;
    }

    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}