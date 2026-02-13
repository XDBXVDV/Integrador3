package com.integrador.toishan.service;

import com.integrador.toishan.dto.createDTO.ClienteCreateDTO;
import com.integrador.toishan.dto.createDTO.UsuarioCreateDTO;
import com.integrador.toishan.dto.updateDTO.ClienteUpdateDTO;
import com.integrador.toishan.model.Cliente;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.ClienteRepo;
import com.integrador.toishan.repo.UsuarioRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder passwordEncoder;

    public Cliente crearCliente(ClienteCreateDTO dto) {

        UsuarioCreateDTO userDTO = new UsuarioCreateDTO();
        userDTO.setUsuario(dto.getUsuario());
        userDTO.setEmail(dto.getEmail());
        userDTO.setContrasena(dto.getContrasena());
        userDTO.setIdRol(1L);

        Usuario usuario = usuarioService.crearUsuario(userDTO);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTelefono(dto.getTelefono());

        return clienteRepo.save(cliente);
    }

    public Cliente editarCliente(Long idCliente, ClienteUpdateDTO dto) {

        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario usuario = cliente.getUsuario();
        if (!usuario.getUsuario().equals(dto.getUsuario())
                && usuarioRepo.existsByUsuario(dto.getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }


        if (!usuario.getEmail().equals(dto.getEmail())
                && usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            usuario.setContrasena(
                    passwordEncoder.encode(dto.getContrasena())
            );
        }


        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());

        cliente.setTelefono(dto.getTelefono());

        usuarioRepo.save(usuario);
        return clienteRepo.save(cliente);
    }

}
