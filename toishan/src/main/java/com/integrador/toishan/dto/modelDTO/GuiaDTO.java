package com.integrador.toishan.dto.createDTO;

public class GuiaDTO {
  private String NumeroGuia;
    private String tipoMovimiento; // "ENTRADA" o "SALIDA"
    private String motivo;
    private Long idEmpleadoAlmacen;
    private Long idDocumentoReferencia;
    private List<ItemGuiaDTO> items;
}
