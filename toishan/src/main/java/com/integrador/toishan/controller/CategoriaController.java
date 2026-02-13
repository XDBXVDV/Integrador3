package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.CategoriaDTO;
import com.integrador.toishan.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.crear(dto));
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
}
