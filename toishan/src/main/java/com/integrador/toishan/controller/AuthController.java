package com.integrador.toishan.controller;

import com.integrador.toishan.dto.createDTO.LoginResponseDto;
import com.integrador.toishan.dto.modelDTO.LoginDto;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        try {
            // El servicio ahora retorna un LoginResponseDto con idPersona (Cliente o Empleado)
            LoginResponseDto response = authService.login(dto);

            // Retornamos el objeto con estatus 200 OK
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Si el usuario no existe o la contraseña falla (mensajes del AuthService)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e) {
            // Error genérico para cualquier otro fallo inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor: " + e.getMessage());
        }
    }
}