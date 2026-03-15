package com.integrador.toishan.controller;

import com.integrador.toishan.model.EstadoOrden;
import com.integrador.toishan.model.OrdenCompra;
import com.integrador.toishan.service.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenService;

    // Ruta: GET /api/ordenes/estado/EMITIDA
    // El JS usa esto para llenar la tabla inicial
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenCompra>> listarPorEstado(@PathVariable String estado) {
        try {
            // Convertimos el String de la URL al Enum EstadoOrden
            EstadoOrden estadoEnum = EstadoOrden.valueOf(estado.toUpperCase());
            List<OrdenCompra> lista = ordenService.listarPorEstado(estadoEnum);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Ruta: GET /api/ordenes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }
}