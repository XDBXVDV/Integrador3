package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.ClienteCreateDTO;
import com.integrador.toishan.dto.updateDTO.ClienteUpdateDTO;
import com.integrador.toishan.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody ClienteCreateDTO dto) {
        return ResponseEntity.ok(clienteService.crearCliente(dto));
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody ClienteUpdateDTO dto) {
        return ResponseEntity.ok(clienteService.editarCliente(id, dto));
    }
}

