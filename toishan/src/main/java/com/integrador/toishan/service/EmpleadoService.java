package com.integrador.toishan.service;


import com.integrador.toishan.dto.createDTO.EmpleadoCreateDto;
import com.integrador.toishan.dto.createDTO.UsuarioCreateDto;
import com.integrador.toishan.dto.modelDTO.EmpleadoDTO;
import com.integrador.toishan.dto.updateDTO.EmpleadoUpdateDto;
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

import java.util.Collection;

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

    public Empleado buscarEmpleado(Long id){
        return empleadoRepo.findById(id).orElse(null);
    }

    public Collection<Empleado>  buscarEmpleados(){
        return empleadoRepo.findAll();
    }

    public Empleado crearEmpleado(EmpleadoCreateDto dto) {
        UsuarioCreateDto userDto = new UsuarioCreateDto();
        userDto.setUsuario(dto.getUsuario());
        userDto.setEmail(dto.getEmail());
        userDto.setContrasena(dto.getContrasena());
        userDto.setIdRol(dto.getIdRol());

        Usuario usuario = usuarioService.crearUsuario(userDto);

        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setDni(dto.getDni());
        return empleadoRepo.save(empleado);
    }

   public Empleado actualizarEmpleado(Long id, EmpleadoUpdateDto dto) {
        Empleado e=empleadoRepo.findById(id).orElseThrow(() -> new RuntimeException("Empleado no existe"));
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setDni(dto.getDni());
        return empleadoRepo.save(e);
   }

}
