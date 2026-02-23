package com.integrador.toishan.controller;


import com.integrador.toishan.model.Venta;
import com.integrador.toishan.service.VentaDtoService;
import com.integrador.toishan.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaDtoService ventaDtoService;

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(ventaDtoService.ListarVentas() );
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id_venta){
        Venta venta= ventaService.findbyid(id_venta);
        if(venta!=null){
            return ResponseEntity.ok(venta);
        } else return ResponseEntity.notFound().build();
    }

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

