package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.DevolucionCreateDTO;
import com.integrador.toishan.service.DevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devolucione")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody DevolucionCreateDTO dto) {
        return ResponseEntity.ok(devolucionService.crearDevolucion(dto));
    }

    @PutMapping("/anular/{id}")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        devolucionService.anularDevolucion(id);
        return ResponseEntity.ok("Devolución anulada");
    }
}

