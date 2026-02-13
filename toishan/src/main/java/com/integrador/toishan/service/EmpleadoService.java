package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.EmpleadoCreateDTO;
import com.integrador.toishan.dto.createDTO.UsuarioCreateDTO;
import com.integrador.toishan.dto.updateDTO.EmpleadoUpdateDTO;
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

    public Empleado crearEmpleado(EmpleadoCreateDTO dto) {

        UsuarioCreateDTO userDTO = new UsuarioCreateDTO();
        userDTO.setUsuario(dto.getUsuario());
        userDTO.setEmail(dto.getEmail());
        userDTO.setContrasena(dto.getContrasena());
        userDTO.setIdRol(dto.getIdRol());

        Usuario usuario = usuarioService.crearUsuario(userDTO);

        Empleado empleado = new Empleado();
        empleado.setUsuario(usuario);
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setDni(dto.getDni());

        return empleadoRepo.save(empleado);
    }

    public Empleado editarEmpleado(Long idEmpleado, EmpleadoUpdateDTO dto) {

        Empleado empleado = empleadoRepo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        Usuario usuario = empleado.getUsuario();

        if (!usuario.getUsuario().equals(dto.getUsuario())
                && usuarioRepo.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }


        if (!usuario.getEmail().equals(dto.getEmail())
                && usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepo.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no válido"));

        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(rolRepo.findById(dto.getIdRol()).orElseThrow());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            usuario.setContrasena(
                    passwordEncoder.encode(dto.getContrasena())
            );
        }


        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());


        usuarioRepo.save(usuario);
        return empleadoRepo.save(empleado);
    }

}
