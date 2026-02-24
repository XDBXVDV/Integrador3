package com.integrador.toishan.dto.updateDTO;

import com.integrador.toishan.model.Estado;

public class UsuarioUpdateDto {
    private String usuario;
    private String email;
    private Estado estado;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
