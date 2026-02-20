package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
    @Table(name = "empleados")
    public class Empleado {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_empleado")
        private Long idEmpleado;

        @OneToOne
        @JoinColumn(name = "id_usuario", nullable = false)
        private Usuario usuario;

        @Column(nullable = false)
        private String nombre;

        @Column(nullable = false)
        private String apellido;

        private String dni;
    @JsonIgnore
    @OneToMany(mappedBy = "empleado")
    private List<Venta> ventas;

        public Empleado(Long idEmpleado, Usuario usuario, String nombre, String apellido, String dni) {
            this.idEmpleado = idEmpleado;
            this.usuario = usuario;
            this.nombre = nombre;
            this.apellido = apellido;
            this.dni = dni;
        }

        public Empleado() {
        }

        public Long getIdEmpleado() {
            return idEmpleado;
        }

        public void setIdEmpleado(Long idEmpleado) {
            this.idEmpleado = idEmpleado;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }
    }


