package com.integrador.toishan.controller;


import com.integrador.toishan.model.Empleado;
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
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.crearEmpleado(empleado));
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Empleado empleado)
    {
        return ResponseEntity.ok(empleadoService.editarEmpleado(id, empleado));
    }
}