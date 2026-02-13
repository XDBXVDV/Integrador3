package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.VentaCreateDTO;
import com.integrador.toishan.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody VentaCreateDTO dto) {
        return ResponseEntity.ok(ventaService.crearVenta(dto));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        ventaService.anularVenta(id);
        return ResponseEntity.ok("Venta anulada");
    }
}

