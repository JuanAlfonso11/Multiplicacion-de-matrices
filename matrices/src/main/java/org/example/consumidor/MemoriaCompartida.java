package org.example.consumidor;

import org.example.mensaje.Mensaje;

import java.util.ArrayList;
import java.util.concurrent.*;

public class MemoriaCompartida extends Consumidor {
    private int[][] A;
    private int[][] B;
    private int[][] C;

    @Override
    public void procesarMensaje(Mensaje mensaje) {
        if (mensaje.getContenido() instanceof ArrayList) {
            ArrayList<?> otra = (ArrayList<?>) mensaje.getContenido();
            A = (int[][]) otra.get(0);
            B = (int[][]) otra.get(1);
            C = new int[A.length][B[0].length];
            System.out.println("Memoria Compartida: ");

            long startTime = System.nanoTime();

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<int[][]> future1 = executor.submit(new MatrixMultiplicationTask(A, B, 0, A.length / 2));
            Future<int[][]> future2 = executor.submit(new MatrixMultiplicationTask(A, B, A.length / 2, A.length));

            try {
                int[][] partialC1 = future1.get();
                int[][] partialC2 = future2.get();

                combineResults(partialC1, 0);
                combineResults(partialC2, A.length / 2);

                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
                System.out.println("Tiempo de ejecución: " + duration + " ms");

                System.out.println("Resultado:");
                imprimir();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            executor.shutdown();
        }
    }

    private void combineResults(int[][] partialC, int startRow) {
        for (int i = 0; i < partialC.length; i++) {
            System.arraycopy(partialC[i], 0, C[startRow + i], 0, partialC[i].length);
        }
    }

    public void imprimir() {
        for (int[] row : C) {
            for (int elem : row) {
                System.out.print(" " + elem + " ");
            }
            System.out.println();
        }
    }

    private static class MatrixMultiplicationTask implements Callable<int[][]> {
        private final int[][] A;
        private final int[][] B;
        private final int startRow;
        private final int endRow;

        public MatrixMultiplicationTask(int[][] A, int[][] B, int startRow, int endRow) {
            this.A = A;
            this.B = B;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public int[][] call() {
            int[][] result = new int[endRow - startRow][B[0].length];
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < B[0].length; j++) {
                    for (int k = 0; k < A[0].length; k++) {
                        result[i - startRow][j] += A[i][k] * B[k][j];
                    }
                }
            }
            return result;
        }
    }
}
