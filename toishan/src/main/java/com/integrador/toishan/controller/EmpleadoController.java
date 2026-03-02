package com.integrador.toishan.controller;


import com.integrador.toishan.dto.updateDTO.EmpleadoUpdateDto;
import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.service.EmpleadoDetalleDtoService;
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

    @Autowired
    private EmpleadoDetalleDtoService detalleDtoService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarEmpleados(){
        return ResponseEntity.ok(empleadoDtoService.getEmpleados());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarEmpleado(@PathVariable Long id){
return ResponseEntity.ok(detalleDtoService.obtenerDetalle(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.crearEmpleado(empleado));
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Long id, @RequestBody EmpleadoUpdateDto dto)
    {
        return ResponseEntity.ok(empleadoService.actualizarEmpleado(id,dto));
    }
}