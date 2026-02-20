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

        return empleadoRepo.findById(idEmpleado).map(empleado -> {
            empleado.setNombre(empleado1.getNombre());
            empleado.setApellido(empleado1.getApellido());
            return  empleadoRepo.save(empleado);
        }).orElseThrow(() -> new RuntimeException("Empleado no existe"));

    }

}
