package com.integrador.toishan.dto.updateDTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordUpdateDto {
    @JsonProperty("idUsuario")
    private Long idUsuario;

    @JsonProperty("passwordActual")
    private String passwordActual;

    @JsonProperty("passwordNueva")
    private String passwordNueva;

    @JsonCreator
    public PasswordUpdateDto(
            @JsonProperty("idUsuario") Long idUsuario,
            @JsonProperty("passwordActual") String passwordActual,
            @JsonProperty("passwordNueva") String passwordNueva
    ) {
        this.idUsuario = idUsuario;
        this.passwordActual = passwordActual;
        this.passwordNueva = passwordNueva;
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public String getPasswordNueva() {
        return passwordNueva;
    }

    public void setPasswordNueva(String passwordNueva) {
        this.passwordNueva = passwordNueva;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {}
}
