package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.ClienteDTO;
import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.repo.ClienteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClienteDtoService {
    @Autowired
    private ClienteRepo clienteRepo;

    public List<ClienteDTO> listaCliente(){
        List<Cliente> clientes= (List<Cliente>) clienteRepo.findAll();
        return clientes.stream().map(this::mapToDTO).toList();
    }

    private ClienteDTO mapToDTO(Cliente cliente){
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setIdCliente(cliente.getIdCliente());
        clienteDTO.setIdUsuario(cliente.getUsuario().getIdUsuario());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellido(cliente.getApellido());
        clienteDTO.setDni(cliente.getDni());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setEstadoUsuario(cliente.getUsuario().getEstado().toString());
        return clienteDTO;
    }
}
