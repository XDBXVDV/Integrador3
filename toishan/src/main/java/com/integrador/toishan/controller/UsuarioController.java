package com.integrador.toishan.controller;


import com.integrador.toishan.dto.createDTO.UsuarioCreateDto;
import com.integrador.toishan.dto.updateDTO.PasswordUpdateDto;
import com.integrador.toishan.dto.updateDTO.UsuarioUpdateDto;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id){
        Usuario buscar= usuarioService.buscarUsuario(id);
        if (buscar!=null){
            return ResponseEntity.ok(buscar);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioCreateDto dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDto dto) {

        Usuario usuario = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/actualizar-contrasena")
    public ResponseEntity<?> actualizarContrasena(@RequestBody PasswordUpdateDto dto) {
        usuarioService.actualizarContrasena(dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok("Usuario desactivado");
    }
    @PutMapping("/reactivar/{id}")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        usuarioService.reactivarUsuario(id);
        return ResponseEntity.ok("Usuario reactivado");
    }
}

