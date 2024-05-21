package org.example;

import org.example.mensaje.Mensaje;
import org.example.canal.Canal;
import org.example.consumidor.MemoriaCompartida;
import org.example.consumidor.MemoriaDistribuida;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {

        int size = 2;
        int[][] A = new int[size][size];
        int[][] B = new int[size][size];
        ArrayList<int[][]> lista = new ArrayList<>();
        iniciarMatriz(A);
        System.out.println();
        iniciarMatriz(B);
        System.out.println();
        lista.add(A);
        lista.add(B);
        Canal canal = new Canal();

        // Crear el ExecutorService para ejecutar consumidores en paralelo
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Crear y registrar consumidores
        MemoriaDistribuida memoriaDistribuida = new MemoriaDistribuida();
        MemoriaCompartida memoriaCompartida = new MemoriaCompartida();
        canal.registrarConsumidor(memoriaDistribuida);
        canal.registrarConsumidor(memoriaCompartida);

        // Crear mensaje
        Mensaje<ArrayList> msg2 = new Mensaje<>(lista);

        // Ejecutar procesamiento en paralelo y medir el tiempo de ejecución para cada consumidor
        try {
            Future<Long> future1 = executorService.submit(() -> {
                long startTime = System.nanoTime();
                memoriaDistribuida.procesarMensaje(msg2);
                long endTime = System.nanoTime();
                return (endTime - startTime) / 1_000_000; // Convertir a milisegundos
            });

            Future<Long> future2 = executorService.submit(() -> {
                long startTime = System.nanoTime();
                memoriaCompartida.procesarMensaje(msg2);
                long endTime = System.nanoTime();
                return (endTime - startTime) / 1_000_000; // Convertir a milisegundos
            });

            // Obtener y mostrar los tiempos de ejecución
            long duration1 = future1.get();
            long duration2 = future2.get();
            System.out.println("\nTiempo de ejecución MemoriaDistribuida: " + duration1 + " ms");
            System.out.println("Tiempo de ejecución MemoriaCompartida: " + duration2 + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public static void iniciarMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = (int) (Math.random() * 10);
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
}
