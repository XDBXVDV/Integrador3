package com.integrador.toishan.controller;

import com.integrador.toishan.dto.modelDTO.MarcaDTO;
import com.integrador.toishan.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marca")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody MarcaDTO dto) {
        return ResponseEntity.ok(marcaService.crear(dto));
    }
    @PutMapping("/desactivar/{id_marca}")
            public ResponseEntity<?> desactivar(@PathVariable Long id_marca){
        marcaService.desactivar(id_marca);
        return ResponseEntity.ok("Marca Desactivada");
    }
    @PutMapping("/activar/{id_marca}")
    public ResponseEntity<?> activar(@PathVariable Long id_marca){
        marcaService.activar(id_marca);
        return ResponseEntity.ok("Marca reactivada");
    }
}

