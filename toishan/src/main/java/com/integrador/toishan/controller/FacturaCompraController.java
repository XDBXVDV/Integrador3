package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.FacturaRequestDTO;
import com.integrador.toishan.model.FacturaCompra;
import com.integrador.toishan.service.FacturaCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaCompraController {

    @Autowired
    private FacturaCompraService facturaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarFactura(
            @RequestParam Long idOrden,
            @RequestParam String serie,
            @RequestParam String correlativo,
            @RequestParam(required = false) MultipartFile archivo
    ) throws IOException {
        try {
            facturaService.registrarFacturaYSubirStock(idOrden, serie, correlativo, archivo);
            return ResponseEntity.ok("Factura registrada e inventario actualizado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<FacturaCompra>> listar() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }
}