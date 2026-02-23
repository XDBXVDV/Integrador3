package com.integrador.toishan.controller;


import com.integrador.toishan.model.Producto;
import com.integrador.toishan.service.ProductoDtoService;
import com.integrador.toishan.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoDtoService productoDtoService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProductos() {
        return ResponseEntity.ok(productoDtoService.listaProducto());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id){
        Producto producto = productoService.findById(id);
        if(producto!=null){
            return ResponseEntity.ok(producto);
        } else return ResponseEntity.notFound().build();

    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Producto producto){
        return ResponseEntity.ok(productoService.crear(producto));
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {

        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }
}
