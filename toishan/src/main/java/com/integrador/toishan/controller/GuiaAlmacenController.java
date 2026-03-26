package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.GuiaDTO;
import com.integrador.toishan.model.DetalleGuia;
import com.integrador.toishan.model.Guia;
import com.integrador.toishan.model.Producto;
import com.integrador.toishan.repo.DetalleGuiaRepo;
import com.integrador.toishan.repo.GuiaRepo;
import com.integrador.toishan.repo.ProductoRepo;
import com.integrador.toishan.service.GuiaAlmacenService;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
@CrossOrigin(origins = "*")
public class GuiaAlmacenController {

    @Autowired
    private GuiaAlmacenService guiaService;
    @Autowired
    private GuiaRepo guiaRepo;
    @Autowired
    private DetalleGuiaRepo detalleRepo;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody GuiaDTO dto) {
        try {
            Guia g = guiaService.procesarGuia(dto);
            return ResponseEntity.ok("Guía " + g.getNumeroGuia() + " procesada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Guia>> listar() {
        return ResponseEntity.ok(guiaRepo.findAll());
    }

    @GetMapping("/detalles/{idGuia}")
    public ResponseEntity<List<DetalleGuia>> verDetalles(@PathVariable Long idGuia) {
        return ResponseEntity.ok(detalleRepo.buscarPorGuia(idGuia));
    }
    @GetMapping("/pdf/{idGuia}")
    public void generarPdf(@PathVariable Long idGuia, HttpServletResponse response) throws IOException {
        Guia guia = guiaRepo.findById(idGuia)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        List<DetalleGuia> detalles = detalleRepo.buscarPorGuia(idGuia);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=Guia_" + guia.getNumeroGuia() + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Encabezado
        document.add(new Paragraph("TOISHAN - SISTEMA DE ALMACÉN").setFontSize(18).setBold());
        document.add(new Paragraph("GUÍA DE MOVIMIENTO: " + guia.getNumeroGuia()));
        document.add(new Paragraph("Tipo: " + guia.getTipoMovimiento() + " | Motivo: " + guia.getMotivo()));
        document.add(new Paragraph("Fecha: " + guia.getFechaMovimiento().toString()));
        document.add(new Paragraph("Responsable: " + guia.getEmpleadoAlmacen().getNombre()));
        document.add(new Paragraph("\n"));

        // Tabla de Productos
        Table table = new Table(new float[]{3, 1});
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell("Producto");
        table.addHeaderCell("Cantidad");

        for (DetalleGuia d : detalles) {
            table.addCell(d.getProducto().getNombre());
            table.addCell(String.valueOf(d.getCantidad()));
        }

        document.add(table);

        document.add(new Paragraph("\n\n\n"));
        document.add(new Paragraph("__________________________          __________________________"));
        document.add(new Paragraph("       Firma Almacén                        Firma Receptor"));

        document.close();
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<Guia>> obtenerReporteMovimientos(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(required = false) String tipo) {

        try {
            List<Guia> lista = guiaService.obtenerReporte(inicio, fin, tipo);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reporte-pdf-consolidado")
    public void exportarReporteConsolidado(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam(required = false) String tipo,
            HttpServletResponse response) throws IOException {

        // 1. Obtener la misma lista de movimientos que ve el usuario en pantalla
        List<Guia> movimientos = guiaService.obtenerReporte(inicio, fin, tipo);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=Reporte_Movimientos.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate()); // Horizontal para mejor lectura

        // --- ENCABEZADO ---
        document.add(new Paragraph("TOISHAN - REPORTE CONSOLIDADO DE MOVIMIENTOS").setBold().setFontSize(18));
        document.add(new Paragraph("Periodo: " + inicio + " al " + fin));
        document.add(new Paragraph("Filtro Tipo: " + (tipo != null && !tipo.isEmpty() ? tipo : "TODOS")));
        document.add(new Paragraph("\n"));

        // --- TABLA ---
        Table table = new Table(new float[]{2, 2, 2, 3, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("FECHA").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("N° GUÍA").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("TIPO").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("MOTIVO").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("RESPONSABLE").setBold()));

        for (Guia g : movimientos) {
            table.addCell(g.getFechaMovimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            table.addCell(g.getNumeroGuia());
            table.addCell(g.getTipoMovimiento());
            table.addCell(g.getMotivo());
            table.addCell(g.getEmpleadoAlmacen().getNombre());
        }

        document.add(table);

        long entradas = movimientos.stream().filter(m -> m.getTipoMovimiento().equals("ENTRADA")).count();
        long salidas = movimientos.stream().filter(m -> m.getTipoMovimiento().equals("SALIDA")).count();

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("RESUMEN DEL PERIODO:").setBold());
        document.add(new Paragraph("Total de Entradas registradas: " + entradas));
        document.add(new Paragraph("Total de Salidas registradas: " + salidas));

        document.close();
    }

}