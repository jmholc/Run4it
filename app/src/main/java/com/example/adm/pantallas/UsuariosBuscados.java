package com.example.adm.pantallas;

public class UsuariosBuscados{

    String username, nombreapellido, estado, idusuario;

    public UsuariosBuscados(String username, String nombreapellido, String estado, String idusuario) {
        this.username = username;
        this.nombreapellido = nombreapellido;
        this.estado = estado;
        this.idusuario = idusuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreapellido() {
        return nombreapellido;
    }

    public void setNombreapellido(String nombreapellido) {
        this.nombreapellido = nombreapellido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }
}
