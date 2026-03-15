package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.CotizacionDTO;
import com.integrador.toishan.model.Cotizacion;
import com.integrador.toishan.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "*")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody CotizacionDTO dto) {
        try {
            return ResponseEntity.ok(cotizacionService.guardarCotizacion(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Cotizacion>> listarPendientes() {
        List<Cotizacion> pendientes = cotizacionService.listarPendientes();
        return ResponseEntity.ok(pendientes);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobar(@PathVariable Long id) {
        try {
            cotizacionService.aprobarCotizacion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para rechazar
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id) {
        try {
            cotizacionService.rechazarCotizacion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
