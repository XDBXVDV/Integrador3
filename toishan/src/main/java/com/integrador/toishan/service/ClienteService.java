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


        if (clienteRepo.existsByUsuario(usuario)) {
            throw new RuntimeException("Este usuario ya está asociado a un cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());

        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());

        return clienteRepo.save(cliente);
    }

    public Cliente editarCliente(Long idCliente, Cliente editar) {

        return clienteRepo.findById(idCliente).map(cliente -> {


            cliente.setNombre(editar.getNombre());
            cliente.setApellido(editar.getApellido());
            cliente.setDni(editar.getDni());
            cliente.setTelefono(editar.getTelefono());
            cliente.setDireccion(editar.getDireccion());


            return clienteRepo.save(cliente);

        }).orElseThrow(() -> new RuntimeException("Cliente con ID " + idCliente + " no encontrado"));
    }

}
