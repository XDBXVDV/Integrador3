package com.integrador.toishan.dto.createDTO;

import java.util.List;

public class ConversionPedidoDTO {
    private List<Long> idsRequerimientos;
    private Long idProveedor;
    private Long idEmpleadoLogistica;
    public ConversionPedidoDTO() {}
    // Getters y Setters obligatorios
    public List<Long> getIdsRequerimientos() { return idsRequerimientos; }
    public void setIdsRequerimientos(List<Long> idsRequerimientos) { this.idsRequerimientos = idsRequerimientos; }
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }
    public Long getIdEmpleadoLogistica() { return idEmpleadoLogistica; }
    public void setIdEmpleadoLogistica(Long idEmpleadoLogistica) { this.idEmpleadoLogistica = idEmpleadoLogistica; }
}