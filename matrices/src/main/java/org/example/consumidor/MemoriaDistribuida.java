package org.example.consumidor;

import org.example.mensaje.Mensaje;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MemoriaDistribuida extends Consumidor {

    @Override
    public void procesarMensaje(Mensaje mensaje) {
        if (mensaje.getContenido() instanceof ArrayList) {
            ArrayList<?> otra = (ArrayList<?>) mensaje.getContenido();
            int[][] A = (int[][]) otra.get(0);
            int[][] B = (int[][]) otra.get(1);
            int[][] C = new int[A.length][B[0].length];
            ExecutorService executor = Executors.newFixedThreadPool(2);
            System.out.println("Memoria Distribuida: ");

            long startTime = System.nanoTime();

            int middle = A.length / 2;
            executor.execute(() -> multiply(A, B, C, 0, middle, "Hilo1"));
            executor.execute(() -> multiply(A, B, C, middle, A.length, "Hilo2"));

            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
            System.out.println("Tiempo de ejecución: " + duration + " ms");

            System.out.println("Resultado:");
            imprimir(C);
        }
    }

    public void imprimir(int[][] C) {
        for (int[] row : C) {
            for (int elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    public void multiply(int[][] A, int[][] B, int[][] C, int startRow, int endRow, String threadName) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
                System.out.println(threadName + " -> C[" + i + "][" + j + "] = " + C[i][j]);
            }
        }
    }
}
