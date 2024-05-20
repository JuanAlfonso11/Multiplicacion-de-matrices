package org.example.productor;

import org.example.canal.Canal;
import org.example.mensaje.Mensaje;

public abstract class Productor {
    private Canal canal;
    public Productor(Canal canal){
        this.canal = canal;
    }
    public void publicar(Mensaje mensaje){
        canal.pasarMensaje(mensaje);
    }
}
