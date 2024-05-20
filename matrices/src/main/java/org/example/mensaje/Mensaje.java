package org.example.mensaje;

public class Mensaje<T> {
    public Mensaje(T contenido) {
        this.contenido = contenido;
    }
    private T contenido;
    public void setContenido(T contenido) {
        this.contenido = contenido;
    }
    public T getContenido() {
        return contenido;
    }
}
