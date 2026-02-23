package com.integrador.toishan.service;

import com.integrador.toishan.dto.modelDTO.UsuarioDTO;
import com.integrador.toishan.model.Usuario;
import com.integrador.toishan.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsuarioDtoService {
    @Autowired
    private UsuarioRepo usuarioRepo;

    private List<UsuarioDTO> listaUsuario(){
        List<Usuario> usuarios= (List<Usuario>) usuarioRepo.findAll();
        return usuarios.stream().map(this::mapToDTO).toList();
    }

    private UsuarioDTO mapToDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRol(usuario.getRol().getRolName());
        return usuarioDTO;
    }
}
