package com.integrador.toishan.service;


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

    public Cliente crearCliente(Cliente dto) {

        Usuario usuario = usuarioRepo.findById(dto.getUsuario().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        // VALIDACIÓN: evitar duplicado
        if (clienteRepo.existsByUsuario(usuario)) {
            throw new RuntimeException("Este usuario ya está asociado a un cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());

        return clienteRepo.save(cliente);
    }

    public Cliente editarCliente(Long idCliente, Cliente editar) {

        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario usuario = cliente.getUsuario();
        if (!usuario.getUsuario().equals(editar.getUsuario())
                && usuarioRepo.existsByUsuario(editar.getUsuario().getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }


        if (!usuario.getEmail().equals(editar.getUsuario().getEmail())
                && usuarioRepo.existsByEmail(editar.getUsuario().getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        usuario.setUsuario(editar.getUsuario().getUsuario());
        usuario.setEmail(editar.getUsuario().getEmail());

        if (editar.getUsuario().getContrasena() != null && !editar.getUsuario().getContrasena().isBlank()) {
            usuario.setContrasena(
                    passwordEncoder.encode(editar.getUsuario().getContrasena())
            );
        }


        cliente.setNombre(editar.getNombre());
        cliente.setApellido(editar.getApellido());

        cliente.setTelefono(editar.getTelefono());

        usuarioRepo.save(usuario);
        return clienteRepo.save(cliente);
    }

}
