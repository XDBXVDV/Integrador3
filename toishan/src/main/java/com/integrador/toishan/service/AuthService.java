package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.LoginResponseDto;
import com.integrador.toishan.dto.modelDTO.LoginDto;
import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.ClienteRepo;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UsuarioRepo usuarioRepo;
    @Autowired private ClienteRepo clienteRepo;
    @Autowired private EmpleadoRepo empleadoRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginDto dto) {
        Usuario usuario = usuarioRepo.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        LoginResponseDto response = new LoginResponseDto();
        response.setIdUsuario(usuario.getIdUsuario());
        response.setUsuario(usuario.getUsuario());

        String nombreRol = usuario.getRol().getRolName().toUpperCase();
        response.setRolName(nombreRol);

        if (nombreRol.equals("CLIENTE")) {
            Cliente c = clienteRepo.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Perfil de cliente no existe"));
            response.setIdPersona(c.getIdCliente());
        }
        else {
            Empleado e = empleadoRepo.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Perfil de empleado no existe"));
            response.setIdPersona(e.getIdEmpleado());
        }

        return response;
    }
}