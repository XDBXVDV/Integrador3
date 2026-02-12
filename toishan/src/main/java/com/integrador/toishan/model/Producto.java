package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private long idproducto;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Double precio;
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;
    @Column(nullable = false)
    private Integer stock;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    enum Estado {
        Activo, Inactivo
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "condicion")
    private Condicion condicion;
    enum Condicion {
        EN_STOCK,
        STOCK_BAJO,
        AGOTADO
    }
@OneToMany(mappedBy = "producto")
private List<DetalleVenta> detalleVentas;

    public Producto(long idproducto, String nombre, Double precio, Integer stockMinimo, Integer stock, Estado estado, Condicion condicion, Categoria categoria, Marca marca) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stockMinimo = stockMinimo;
        this.stock = stock;
        this.estado = Estado.Activo;
        this.condicion = condicion;
        this.categoria = categoria;
        this.marca = marca;
    }

    public Producto() {
    }

    public long getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(long idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}
