package com.integrador.toishan.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas_compra")
public class FacturaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacturaCompra;

    @OneToOne
    @JoinColumn(name = "id_orden_compra", nullable = false)
    private OrdenCompra ordenCompra;

    private String serieFactura;
    private String correlativoFactura;

    @Column(insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    private BigDecimal totalPagado;

    private String pdfUrl;

    public FacturaCompra() {}

    public FacturaCompra(Long idFacturaCompra, OrdenCompra ordenCompra, String serieFactura, String correlativoFactura, LocalDateTime fechaRegistro, BigDecimal totalPagado, String pdfUrl) {
        this.idFacturaCompra = idFacturaCompra;
        this.ordenCompra = ordenCompra;
        this.serieFactura = serieFactura;
        this.correlativoFactura = correlativoFactura;
        this.fechaRegistro = fechaRegistro;
        this.totalPagado = totalPagado;
        this.pdfUrl = pdfUrl;
    }

    public Long getIdFacturaCompra() {
        return idFacturaCompra;
    }

    public void setIdFacturaCompra(Long idFacturaCompra) {
        this.idFacturaCompra = idFacturaCompra;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public String getCorrelativoFactura() {
        return correlativoFactura;
    }

    public void setCorrelativoFactura(String correlativoFactura) {
        this.correlativoFactura = correlativoFactura;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}