package com.integrador.toishan.controller;


import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.service.EmpleadoDtoService;
import com.integrador.toishan.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoDtoService empleadoDtoService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarEmpleados(){
        return ResponseEntity.ok(empleadoDtoService.getEmpleados());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarEmpleado(@PathVariable Long id){
        Empleado empleado = empleadoService.buscarEmpleado(id);
        if(empleado!=null){
            return ResponseEntity.ok(empleado);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

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