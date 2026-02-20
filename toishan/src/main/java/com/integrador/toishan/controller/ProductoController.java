package com.integrador.toishan.controller;


import com.integrador.toishan.model.Producto;
import com.integrador.toishan.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Producto producto){
        return ResponseEntity.ok(productoService.crear(producto));
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {

        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }
}
