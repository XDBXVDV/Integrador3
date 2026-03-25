package com.integrador.toishan.controller;

import com.integrador.toishan.model.FacturaCompra;
import com.integrador.toishan.repo.FacturaCompraRepo;
import com.integrador.toishan.service.ReporteExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/compras")
@CrossOrigin(origins = "*")
public class ReporteCompraController {

    @Autowired
    private FacturaCompraRepo facturaRepo;
    @Autowired
    private ReporteExportService reporteService;

    @GetMapping("/rango")
    public ResponseEntity<?> comprasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(facturaRepo.findByFechaRegistroBetween(inicio, fin));
    }

    @GetMapping("/por-proveedor")
    public ResponseEntity<?> totalPorProveedor() {
        return ResponseEntity.ok(facturaRepo.obtenerTotalesPorProveedor());
    }

    @GetMapping("/exportar/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {

            LocalDateTime start = inicio.atStartOfDay();
            LocalDateTime end = fin.atTime(LocalTime.MAX);

            List<FacturaCompra> facturas = facturaRepo.findByFechaRegistroBetween(start, end);
            byte[] pdfData = reporteService.generarPdfFiltrado(facturas, inicio.toString(), fin.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Toishan_" + inicio + ".pdf");

            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}