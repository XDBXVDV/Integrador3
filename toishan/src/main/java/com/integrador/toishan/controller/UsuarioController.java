package com.integrador.toishan.controller;


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
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return null;
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

