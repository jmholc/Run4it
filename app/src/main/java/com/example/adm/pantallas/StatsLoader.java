package com.example.adm.pantallas;

/**
 * Created by Nietos on 12/11/2017.
 */

public class StatsLoader {

    String descripcion, info;

    public StatsLoader(String descripcion, String info) {
        this.descripcion = descripcion;
        this.info = info;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
