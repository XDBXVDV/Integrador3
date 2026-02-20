package com.integrador.toishan.service;


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

    public Usuario crearUsuario(Usuario u) {

        if (usuarioRepo.existsByUsuario(u.getUsuario())) {
            throw new RuntimeException("Usuario ya existe");
        }
        if (usuarioRepo.existsByEmail(u.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setUsuario(u.getUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setContrasena(passwordEncoder.encode(u.getContrasena()));
        usuario.setRol(rolRepo.findById(u.getRol().getIdrol()).orElse(null));
        usuario.setEstado(Estado.Activo);

        return usuarioRepo.save(usuario);
    }

    public void cambiarContrasena(Long idUsuario ,Usuario u ) {
        Usuario usuario=usuarioRepo.findById(idUsuario).orElse(null);
        if (usuario==null) {
            throw  new RuntimeException("El usuario no existe");
        } else{

            usuario.setContrasena(passwordEncoder.encode(u.getContrasena()));
        }

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
