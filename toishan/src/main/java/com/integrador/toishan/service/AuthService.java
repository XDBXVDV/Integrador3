package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.LoginDto;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario login(LoginDto dto){

        Usuario usuario = usuarioRepo.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())){
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }

}