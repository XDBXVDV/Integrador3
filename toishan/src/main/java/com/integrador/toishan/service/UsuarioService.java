package com.integrador.toishan.service;


import com.integrador.toishan.dto.createDTO.UsuarioCreateDto;
import com.integrador.toishan.dto.updateDTO.PasswordUpdateDto;
import com.integrador.toishan.dto.updateDTO.UsuarioUpdateDto;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Rol;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.RolRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private RolRepo rolRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crearUsuario(UsuarioCreateDto dto) {

        if (usuarioRepo.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Usuario ya existe");
        }

        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        Rol rol = rolRepo.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no existe"));

        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rol);
        usuario.setEstado(Estado.Activo);

        return usuarioRepo.save(usuario);
    }



    public Collection<Usuario> obtenerUsuarios() {
        return usuarioRepo.findAll();
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioRepo.findById(id).orElse(null);
    }

    public Usuario actualizarUsuario(Long idUsuario, UsuarioUpdateDto dto) {

        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        if (!u.getUsuario().equals(dto.getUsuario())
                && usuarioRepo.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (!u.getEmail().equals(dto.getEmail())
                && usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        u.setUsuario(dto.getUsuario());
        u.setEmail(dto.getEmail());
        u.setEstado(dto.getEstado());

        return usuarioRepo.save(u);
    }

    public void desactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();
        usuario.setEstado(Estado.Inactivo);
        usuarioRepo.save(usuario);
    }

    public void reactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();
        usuario.setEstado(Estado.Activo);
        usuarioRepo.save(usuario);
    }

    public void cambiarPassword(Long idUsuario, PasswordUpdateDto dto) {

        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), u.getContrasena())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        u.setContrasena(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepo.save(u);
    }
}
