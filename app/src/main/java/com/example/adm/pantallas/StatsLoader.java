package com.example.adm.pantallas;

/**
 * Created by Nietos on 12/11/2017.
 */

public class StatsLoader {
    String cantidadpasos;
    String velPromedio;
    String elevacion;
    String totaldistancia;
    String velmax;
    String distmax;
    String calorias;
    String duracion;

    public StatsLoader(String cantidadpasos, String velPromedio, String elevacion, String totaldistancia, String velmax, String distmax, String calorias, String duracion) {
        this.cantidadpasos = cantidadpasos;
        this.velPromedio = velPromedio;
        this.elevacion = elevacion;
        this.totaldistancia = totaldistancia;
        this.velmax = velmax;
        this.distmax = distmax;
        this.calorias = calorias;
        this.duracion = duracion;
    }
}
