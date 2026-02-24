package com.integrador.toishan.service;

import com.integrador.toishan.dto.detalleDto.ClienteDetalleDTO;
import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.ClienteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDetalleDtoService {
    @Autowired
    private ClienteRepo clienteRepo;

    public ClienteDetalleDTO obtenerDetalle(Long idCliente) {

        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        Usuario u = cliente.getUsuario();

        ClienteDetalleDTO dto = new ClienteDetalleDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setDni(cliente.getDni());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());

        dto.setIdUsuario(u.getIdUsuario());
        dto.setUsuario(u.getUsuario());
        dto.setEmail(u.getEmail());
        dto.setEstadoUsuario(u.getEstado().name());
        dto.setRol(u.getRol().getRolName());

        return dto;
    }
}
