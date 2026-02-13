package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.UsuarioCreateDTO;
import com.integrador.toishan.model.Estado;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.RolRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private RolRepo rolRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crearUsuario(UsuarioCreateDTO dto) {

        if (usuarioRepo.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("Usuario ya existe");
        }
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rolRepo.findById(dto.getIdRol()).orElseThrow());
        usuario.setEstado(Estado.Activo);

        return usuarioRepo.save(usuario);
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
}
