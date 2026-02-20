package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private long idCategoria;
    @Column( nullable = false)
    private String nombre;
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;
    public Categoria() {
    }

    public Categoria(long idCategoria, String nombre, Estado estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.estado = Estado.Activo;
    }



    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
