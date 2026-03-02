package com.integrador.toishan.dto.updateDTO;

import com.integrador.toishan.model.Estado;

public class UsuarioUpdateDto {
    private Long idUsuario;
    private String usuario;
    private String email;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
