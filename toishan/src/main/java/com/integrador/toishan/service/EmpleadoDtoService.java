package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.EmpleadoDTO;
import com.integrador.toishan.model.Empleado;
import com.integrador.toishan.repo.EmpleadoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmpleadoDtoService {
    @Autowired
    private EmpleadoRepo empleadoRepo;

    public List<EmpleadoDTO> getEmpleados(){
        List<Empleado> empleados= empleadoRepo.findAll();
        return empleados.stream().map(this::mapToDTO).toList();
    }

    private EmpleadoDTO mapToDTO(Empleado empleado){
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setNombre(empleado.getNombre());
        dto.setApellido(empleado.getApellido());
        dto.setDni(empleado.getDni());
        dto.setRol(empleado.getUsuario().getRol().getRolName());
        dto.setEstadoUsuario(empleado.getUsuario().getEstado().toString());
        return dto;
    };

}
