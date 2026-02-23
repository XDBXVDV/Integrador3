package com.integrador.toishan.controller;


import com.integrador.toishan.model.Devolucion;
import com.integrador.toishan.service.DevolucionDtoService;
import com.integrador.toishan.service.DevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/devolucione")
public class DevolucionController {

    @Autowired
    private DevolucionService devolucionService;

    @Autowired
    private DevolucionDtoService devolucionDtoService;



    @GetMapping("/listar")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(devolucionDtoService.findAll());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id){
        Devolucion devolucion= devolucionService.findById(id);
        if(devolucion!=null){
            return ResponseEntity.ok(devolucion);
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Devolucion devolucion) {
        return ResponseEntity.ok(devolucionService.crearDevolucion(devolucion));
    }

    @PutMapping("/anular/{id}")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        devolucionService.anularDevolucion(id);
        return ResponseEntity.ok("Devolución anulada");
    }
}

