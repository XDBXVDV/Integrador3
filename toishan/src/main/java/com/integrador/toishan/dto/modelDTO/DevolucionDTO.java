package com.integrador.toishan.dto.modelDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DevolucionDTO {
    private Long idDevolucion;
    private LocalDateTime fechaDevolucion;
    private BigDecimal totalDevuelto;
    private String estado;
    private Long idVenta;
    private List<DetalleDevolucionDTO> detalles;

    public Long getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(Long idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public BigDecimal getTotalDevuelto() {
        return totalDevuelto;
    }

    public void setTotalDevuelto(BigDecimal totalDevuelto) {
        this.totalDevuelto = totalDevuelto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public List<DetalleDevolucionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucionDTO> detalles) {
        this.detalles = detalles;
    }
}
