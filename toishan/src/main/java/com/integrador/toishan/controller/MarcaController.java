package com.integrador.toishan.controller;

import com.integrador.toishan.model.Marca;
import com.integrador.toishan.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marca")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping("/listar")
    public  ResponseEntity<?> listarMarcas(){
        return ResponseEntity.ok(marcaService.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarMarca(@PathVariable Long id){
        Marca marca= marcaService.findMarcaById(id);
        if(marca!=null){
            return ResponseEntity.ok(marca);
        } else return ResponseEntity.notFound().build();

    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.crear(marca));
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

