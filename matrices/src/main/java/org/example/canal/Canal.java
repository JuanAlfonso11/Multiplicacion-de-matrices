package org.example.canal;

import org.example.consumidor.Consumidor;
import org.example.mensaje.Mensaje;

import java.util.LinkedList;
import java.util.List;

public class Canal {
    private List<Consumidor> consumidores= new LinkedList<>();
    public void registrarConsumidor(Consumidor consumidor){
        consumidores.add(consumidor);
    }
    public void borrarConsumidor(Consumidor consumidor){
        consumidores.remove(consumidor);
    }
    public void pasarMensaje(Mensaje mensaje){
        consumidores.forEach(consumidor -> {
            consumidor.procesarMensaje(mensaje);
        });
    }
}
