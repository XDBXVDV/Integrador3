package com.integrador.toishan.dto.createDTO;

import java.util.List;

public class VentaCreateDTO {
    private Long idCliente;
    private Long idEmpleado;
    private List<DetalleVentaCreateDTO> detalles;

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetalleVentaCreateDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaCreateDTO> detalles) {
        this.detalles = detalles;
    }
}
