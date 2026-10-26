package co.edu.uniquindio.poo.model;

public class Beneficio {
    private final String nombre;
    private final int puntosRequeridos;
    private final String descripcion;

    public Beneficio(String nombre, int puntosRequeridos, String descripcion) {
        this.nombre = nombre;
        this.puntosRequeridos = puntosRequeridos;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosRequeridos() {
        return puntosRequeridos;
    }

    public String getDescripcion() {
        return descripcion;
    }
}