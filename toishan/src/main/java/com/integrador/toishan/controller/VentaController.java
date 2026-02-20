package com.integrador.toishan.controller;


import com.integrador.toishan.model.Venta;
import com.integrador.toishan.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Venta venta) {
        return ResponseEntity.ok(ventaService.crearVenta(venta));
    }

    @PutMapping("/anular/{id_venta}")
    public ResponseEntity<?> anular(@PathVariable Long id_venta) {
        ventaService.anularVenta(id_venta);
        return ResponseEntity.ok("Venta anulada");
    }
}

