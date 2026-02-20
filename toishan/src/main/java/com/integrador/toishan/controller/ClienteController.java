package com.integrador.toishan.controller;


import com.integrador.toishan.model.Cliente;
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
    public ResponseEntity<?> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.crearCliente(cliente));
    }
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.editarCliente(id, cliente));
    }
}

