package com.integrador.toishan.dto.modelDTO;

import com.integrador.toishan.dto.detalleDto.ItemGuiaDTO;
import com.integrador.toishan.model.Movimiento;

import java.util.List;

public class GuiaDTO {
    private String numeroGuia;
    private String tipoMovimiento;
    private String motivo;
    private Long idEmpleadoAlmacen;
    private Long idDocumentoReferencia;
    private List<ItemGuiaDTO> items;

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

    public Long getIdEmpleadoAlmacen() {
        return idEmpleadoAlmacen;
    }

    public void setIdEmpleadoAlmacen(Long idEmpleadoAlmacen) {
        this.idEmpleadoAlmacen = idEmpleadoAlmacen;
    }

    public Long getIdDocumentoReferencia() {
        return idDocumentoReferencia;
    }

    public void setIdDocumentoReferencia(Long idDocumentoReferencia) {
        this.idDocumentoReferencia = idDocumentoReferencia;
    }

    public List<ItemGuiaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemGuiaDTO> items) {
        this.items = items;
    }
}
