package com.integrador.toishan.dto;

import java.util.List;

public class VentaCreateDTO {
    private Long idCliente;
    private List<DetalleVentaCreateDTO> detalles;

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
