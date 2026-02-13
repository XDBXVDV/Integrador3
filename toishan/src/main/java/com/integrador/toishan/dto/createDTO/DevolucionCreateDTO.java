package com.integrador.toishan.dto.createDTO;

import java.util.List;

public class DevolucionCreateDTO {
    private Long idVenta;
    private List<DetalleDevolucionCreateDTO> detalles;

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public List<DetalleDevolucionCreateDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucionCreateDTO> detalles) {
        this.detalles = detalles;
    }
}

