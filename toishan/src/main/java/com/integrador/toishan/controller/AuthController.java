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

            LoginResponseDto response = authService.login(dto);


            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno en el servidor: " + e.getMessage());
        }
    }
}