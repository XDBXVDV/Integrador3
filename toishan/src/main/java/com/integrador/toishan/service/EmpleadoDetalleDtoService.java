package com.integrador.toishan.service;

import com.integrador.toishan.dto.detalleDto.EmpleadoDetalleDTO;
import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.EmpleadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoDetalleDtoService {
    @Autowired
    private EmpleadoRepo empleadoRepo;
    public EmpleadoDetalleDTO obtenerDetalle(Long idEmpleado) {
        Empleado empleado = empleadoRepo.findById(idEmpleado).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        Usuario usuario = empleado.getUsuario();
        EmpleadoDetalleDTO dto = new EmpleadoDetalleDTO();
        dto.setIdEmpleado(idEmpleado);
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setDni(empleado.getDni());
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsuario(usuario.getUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setEstadoUsuario(usuario.getEstado().toString());
        dto.setRol(usuario.getRol().getRolName());
        return dto;
    }
}
