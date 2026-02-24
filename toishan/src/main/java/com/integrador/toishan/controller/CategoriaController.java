package com.integrador.toishan.controller;


import com.integrador.toishan.model.Categoria;
import com.integrador.toishan.model.Marca;
import com.integrador.toishan.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listar")
    public ResponseEntity<?> findAll(){
        return  ResponseEntity.ok().body(categoriaService.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id){
        Categoria categoria = categoriaService.findById(id);
        if(categoria!=null){
            return ResponseEntity.ok().body(categoria);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Categoria  categoria) {
        return ResponseEntity.ok(categoriaService.crear(categoria));
    }

    @PutMapping("/desactivar/{id_categoria}")
    public ResponseEntity<?> desactivar(@PathVariable Long id_categoria) {
        categoriaService.desactivar(id_categoria);
        return ResponseEntity.ok("Categoría desactivada");
    }

    @PutMapping("/activar/{id_categoria}")
    public ResponseEntity<?> activar(@PathVariable Long id_categoria) {
        categoriaService.activar(id_categoria);
        return ResponseEntity.ok("Categoría reactivada");
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@RequestBody Categoria categoria, @PathVariable Long id){
        Categoria categoria1 = categoriaService.findById(id);
        if(categoria1!=null){
            categoria1.setNombre(categoria.getNombre());
            return ResponseEntity.ok(categoriaService.editar(id,categoria1));
        } else return ResponseEntity.notFound().build();
    }

}
