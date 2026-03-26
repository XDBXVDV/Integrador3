package com.integrador.toishan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "guias_almacen")
public class Guia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guia")
    private Long idGuia;
    @Column(name = "numero_guia")
    private String numeroGuia;
    @Column(name = "tipo_movimiento")
    private String tipoMovimiento;
    private String motivo;
    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento = LocalDateTime.now();
    @Column(name = "id_documento_referencia")
    private Long idDocumentoReferencia;

    @ManyToOne
    @JoinColumn(name = "id_empleado_almacen")
    private Empleado empleadoAlmacen;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetalleGuia> detalles;

    public Guia() {}

    public Guia(Long idGuia, String numeroGuia, String tipoMovimiento, String motivo, LocalDateTime fechaMovimiento, Long idDocumentoReferencia, Empleado empleadoAlmacen, List<DetalleGuia> detalles) {
        this.idGuia = idGuia;
        this.numeroGuia = numeroGuia;
        this.tipoMovimiento = tipoMovimiento;
        this.motivo = motivo;
        this.fechaMovimiento = fechaMovimiento;
        this.idDocumentoReferencia = idDocumentoReferencia;
        this.empleadoAlmacen = empleadoAlmacen;
        this.detalles = detalles;
    }

    public Long getIdGuia() {
        return idGuia;
    }

    public void setIdGuia(Long idGuia) {
        this.idGuia = idGuia;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Long getIdDocumentoReferencia() {
        return idDocumentoReferencia;
    }

    public void setIdDocumentoReferencia(Long idDocumentoReferencia) {
        this.idDocumentoReferencia = idDocumentoReferencia;
    }

    public Empleado getEmpleadoAlmacen() {
        return empleadoAlmacen;
    }

    public void setEmpleadoAlmacen(Empleado empleadoAlmacen) {
        this.empleadoAlmacen = empleadoAlmacen;
    }

    public List<DetalleGuia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleGuia> detalles) {
        this.detalles = detalles;
    }
}