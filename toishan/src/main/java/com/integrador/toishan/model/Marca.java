package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "marcas")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long idMarca;
    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Estado estado;
    enum Estado {
        Activo, Inactivo
    }
@OneToMany(mappedBy = "marca")
private List<Producto> productos;
    public Marca(Long idMarca, String nombre, Estado estado) {

        this.idMarca = idMarca;
        this.nombre = nombre;
        this.estado = Estado.Activo;
    }
    public Marca() {}

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
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
