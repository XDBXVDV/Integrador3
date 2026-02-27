package com.integrador.toishan.service;



import com.integrador.toishan.dto.createDTO.ClienteCreateDto;
import com.integrador.toishan.dto.createDTO.UsuarioCreateDto;
import com.integrador.toishan.dto.updateDTO.ClienteUpdateDto;
import com.integrador.toishan.model.Cliente;

import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.ClienteRepo;
import com.integrador.toishan.repo.RolRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
public class ClienteService {
    @Autowired
    private ClienteRepo clienteRepo;
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepo rolRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente findById(Long id){
        return clienteRepo.findById(id).orElse(null);
    }

    public Collection<Cliente> findAll(){
        return clienteRepo.findAll();
    }


    public Cliente crearCliente(ClienteCreateDto dto) {


        UsuarioCreateDto userDto = new UsuarioCreateDto();
        userDto.setUsuario(dto.getUsuario());
        userDto.setEmail(dto.getEmail());
        userDto.setContrasena(dto.getContrasena());
        userDto.setIdRol(dto.getIdRol());

        Usuario usuario = usuarioService.crearUsuario(userDto);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());

        return clienteRepo.save(cliente);
    }


    public Cliente actualizarCliente(Long id, ClienteUpdateDto dto) {
        Cliente c = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        c.setNombre(dto.getNombre());
        c.setApellido(dto.getApellido());
        c.setDni(dto.getDni());
        c.setTelefono(dto.getTelefono());
        c.setDireccion(dto.getDireccion());

        return clienteRepo.save(c);
    }


}
