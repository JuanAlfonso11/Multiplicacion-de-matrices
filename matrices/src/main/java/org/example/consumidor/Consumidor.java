package org.example.consumidor;
import org.example.mensaje.Mensaje;

public abstract class Consumidor {
    public abstract void procesarMensaje(Mensaje mensaje);
}
