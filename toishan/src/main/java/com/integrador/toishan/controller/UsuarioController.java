package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.UsuarioCreateDTO;
import com.integrador.toishan.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok("Usuario desactivado");
    }
}

