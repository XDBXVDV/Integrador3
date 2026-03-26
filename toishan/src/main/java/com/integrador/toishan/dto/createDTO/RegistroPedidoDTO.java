package com.integrador.toishan.dto.createDTO;

import com.integrador.toishan.dto.detalleDto.ItemPedidoDTO;

import java.util.List;

public class RegistroPedidoDTO {
    private Long idProveedor;
    private Long idEmpleado;
    private List<ItemPedidoDTO> items;

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public List<ItemPedidoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoDTO> items) {
        this.items = items;
    }
}
