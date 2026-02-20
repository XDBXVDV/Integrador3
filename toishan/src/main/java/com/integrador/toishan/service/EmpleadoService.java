package com.integrador.toishan.service;


import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.model.Rol;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.EmpleadoRepo;
import com.integrador.toishan.repo.RolRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepo empleadoRepo;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepo rolRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Empleado crearEmpleado(Empleado empleado1) {

        if (empleado1.getUsuario().getIdUsuario() == null) {
            throw new RuntimeException("El idUsuario es obligatorio");
        }

        Usuario usuario = usuarioRepo.findById(empleado1.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        if (empleadoRepo.existsByUsuario(usuario)) {
            throw new RuntimeException("Este usuario ya está asociado a un empleado");
        }

        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);
        empleado.setNombre(empleado1.getNombre());
        empleado.setApellido(empleado1.getApellido());
        empleado.setDni(empleado1.getDni());

        return empleadoRepo.save(empleado);
    }

    public Empleado editarEmpleado(Long idEmpleado, Empleado empleado1) {

        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        Usuario usuario = empleado.getUsuario();

        if (!usuario.getUsuario().equals(empleado1.getUsuario())
                && usuarioRepo.existsByUsuario(empleado1.getUsuario().getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }


        if (!usuario.getEmail().equals(empleado1.getUsuario().getEmail())
                && usuarioRepo.existsByEmail(empleado1.getUsuario().getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepo.findById(empleado1.getUsuario().getRol().getIdrol())
                .orElseThrow(() -> new RuntimeException("Rol no válido"));

        usuario.setUsuario(empleado1.getUsuario().getUsuario());
        usuario.setEmail(empleado1.getUsuario().getEmail());
        usuario.setRol(rolRepo.findById(empleado1.getUsuario().getRol().getIdrol()).orElseThrow());

        if (empleado1.getUsuario().getContrasena() != null && !empleado1.getUsuario().getContrasena().isBlank()) {
            usuario.setContrasena(
                    passwordEncoder.encode(empleado1.getUsuario().getContrasena())
            );
        }


        empleado.setNombre(empleado1.getNombre());
        empleado.setApellido(empleado1.getApellido());


        usuarioRepo.save(usuario);
        return empleadoRepo.save(empleado);
    }

}
