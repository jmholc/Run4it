package com.example.adm.pantallas;

/**
 * Created by adm on 17/11/2017.
 */

public class DesafioPendienteManager {
    String idUsuario;
    String username;
    String objetivodesafio;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getObjetivodesafio() {
        return objetivodesafio;
    }

    public void setObjetivodesafio(String objetivodesafio) {
        this.objetivodesafio = objetivodesafio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    String cantidad;

    public DesafioPendienteManager(String idUsuario, String username, String objetivodesafio, String cantidad) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.objetivodesafio = objetivodesafio;
        this.cantidad = cantidad;
    }


}
