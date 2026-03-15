package com.integrador.toishan.dto.modelDTO;

import java.util.List;

public class CotizacionDTO {
    private Long idPedidoCompra;
    private Long idProveedor;
    private List<ItemCotizacionDTO> items;

    public Long getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(Long idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public List<ItemCotizacionDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCotizacionDTO> items) {
        this.items = items;
    }
}
