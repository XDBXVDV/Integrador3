package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.EmpleadoCreateDTO;
import com.integrador.toishan.dto.updateDTO.EmpleadoUpdateDTO;
import com.integrador.toishan.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody EmpleadoCreateDTO dto) {
        return ResponseEntity.ok(empleadoService.crearEmpleado(dto));
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody EmpleadoUpdateDTO dto)
    {
        return ResponseEntity.ok(empleadoService.editarEmpleado(id, dto));
    }
}