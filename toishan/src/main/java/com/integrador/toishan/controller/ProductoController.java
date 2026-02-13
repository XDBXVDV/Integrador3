package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.ProductoCreateDTO;
import com.integrador.toishan.dto.updateDTO.ProductoUpdateDTO;
import com.integrador.toishan.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoCreateDTO dto) {
        return ResponseEntity.ok(productoService.crear(dto));
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody ProductoUpdateDTO dto) {

        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }
}
