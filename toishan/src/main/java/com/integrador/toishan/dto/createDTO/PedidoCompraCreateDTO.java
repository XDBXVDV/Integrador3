package com.integrador.toishan.dto.createDTO;

import com.integrador.toishan.dto.modelDTO.DetallePedidoCompraDTO;

public class PedidoCompraCreateDTO {
    private Long idProveedor;
    private Long idEmpleado;

    private List<DetallePedidoCompraDTO> detalles;
}
