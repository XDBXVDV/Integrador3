package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.DevolucionCreateDTO;
import com.integrador.toishan.service.DevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody DevolucionCreateDTO dto) {
        return ResponseEntity.ok(devolucionService.crearDevolucion(dto));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        devolucionService.anularDevolucion(id);
        return ResponseEntity.ok("Devolución anulada");
    }
}

