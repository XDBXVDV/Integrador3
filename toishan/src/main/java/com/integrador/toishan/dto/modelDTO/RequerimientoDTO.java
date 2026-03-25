package com.integrador.toishan.dto.modelDTO;

public class RequerimientoDTO {
    private Long idProducto;
    private Integer cantidadSugerida;
    private Long idEmpleadoAlmacen;

    public RequerimientoDTO() {}

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidadSugerida() {
        return cantidadSugerida;
    }

    public void setCantidadSugerida(Integer cantidadSugerida) {
        this.cantidadSugerida = cantidadSugerida;
    }

    public Long getIdEmpleadoAlmacen() {
        return idEmpleadoAlmacen;
    }

    public void setIdEmpleadoAlmacen(Long idEmpleadoAlmacen) {
        this.idEmpleadoAlmacen = idEmpleadoAlmacen;
    }
}
