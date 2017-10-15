package com.example.adm.pantallas;

public class UsuariosBuscados{

    String username, nombreapellido, estado;

    public UsuariosBuscados(String username, String nombreapellido, String estado) {
        this.username = username;
        this.nombreapellido = nombreapellido;
        this.estado = estado;
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
}
