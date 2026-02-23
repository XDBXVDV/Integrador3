package com.integrador.toishan.controller;

import com.integrador.toishan.model.Proveedor;
import com.integrador.toishan.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> bsucar(@PathVariable Long id){
        Proveedor proveedor = proveedorService.findbyid(id);
        if(proveedor!=null){
            return ResponseEntity.ok(proveedor);
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Proveedor proveedor){
        return ResponseEntity.ok(proveedorService.crearProveedor(proveedor));
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivar(@PathVariable Long id){
        Proveedor proveedor = proveedorService.findbyid(id);
        if(proveedor!=null){
            proveedorService.DesactivarProveedor(id);
            return ResponseEntity.ok(proveedor);
        } else return ResponseEntity.notFound().build();
    }

    @PutMapping("/Reactivar/{id}")
    public ResponseEntity<?> reactivar(@PathVariable Long id){
        Proveedor proveedor = proveedorService.findbyid(id);
        if(proveedor!=null){
            proveedorService.DesactivarProveedor(id);
            return ResponseEntity.ok(proveedor);
        } else return ResponseEntity.notFound().build();
    }


}
