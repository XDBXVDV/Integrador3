package com.integrador.toishan.service;

import com.integrador.toishan.model.FacturaCompra;
import com.integrador.toishan.repo.FacturaCompraRepo;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ReporteExportService {

    @Autowired
    private FacturaCompraRepo facturaRepo;

    public byte[] generarPdfFiltrado(List<FacturaCompra> facturas, String fechaInicio, String fechaFin) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Encabezado Personalizado
        document.add(new Paragraph("TOISHAN - REPORTE DE COMPRAS").setBold().setFontSize(16));
        document.add(new Paragraph("Periodo: " + fechaInicio + " al " + fechaFin).setFontSize(10).setItalic());

        // Tabla de datos
        Table table = new Table(new float[]{1, 3, 1, 2, 1}).useAllAvailableWidth();
        table.addHeaderCell("Fecha").addHeaderCell("Proveedor").addHeaderCell("OC").addHeaderCell("Factura").addHeaderCell("Total");

        BigDecimal sumaPeriodo = BigDecimal.ZERO;
        for (FacturaCompra f : facturas) {
            table.addCell(f.getFechaRegistro().toLocalDate().toString());
            table.addCell(f.getOrdenCompra().getCotizacion().getProveedor().getRazonSocial());
            table.addCell(f.getOrdenCompra().getNroOrden());
            table.addCell(f.getSerieFactura() + "-" + f.getCorrelativoFactura());
            table.addCell("S/ " + f.getTotalPagado().toString());
            sumaPeriodo = sumaPeriodo.add(f.getTotalPagado());
        }
        document.add(table);
        document.add(new Paragraph("\nTOTAL DEL PERIODO: S/ " + sumaPeriodo).setBold().setTextAlignment(TextAlignment.RIGHT));

        document.close();
        return out.toByteArray();
    }
}