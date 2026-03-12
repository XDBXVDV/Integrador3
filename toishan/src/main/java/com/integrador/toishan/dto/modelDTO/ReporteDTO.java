package com.integrador.toishan.dto.modelDTO;

public class ReporteDTO {
    private String etiqueta;
    private Number valor;

    public ReporteDTO() {}

    public ReporteDTO(String etiqueta, Number valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;

    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
    }
}
