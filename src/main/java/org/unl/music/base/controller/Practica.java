package org.unl.music.base.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Random;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class Practica {
    private Integer[] matriz;
    private LinkedList<Integer> lista;

    // Constructor
    public static class SortingResult {
        private String algorithm;
        private long[] executionTimes;
        private long averageTime;
        private int dataSize;

        public SortingResult(String algorithm, long[] times, int dataSize) {
            this.algorithm = algorithm;
            this.executionTimes = times;
            this.dataSize = dataSize;
            this.averageTime = calculateAverage(times);
        }

        private long calculateAverage(long[] times) {
            long sum = 0;
            for (long time : times) {
                sum += time;
            }
            return sum / times.length;
        }

        // Getters
        public String getAlgorithm() { return algorithm; }
        public long[] getExecutionTimes() { return executionTimes; }
        public long getAverageTime() { return averageTime; }
        public int getDataSize() { return dataSize; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Algoritmo: %s\n", algorithm));
            sb.append(String.format("Tamaño de datos: %d\n", dataSize));
            sb.append("Tiempos de ejecución (ns): ");
            for (int i = 0; i < executionTimes.length; i++) {
                sb.append(String.format("%d", executionTimes[i]));
                if (i < executionTimes.length - 1) sb.append(", ");
            }
            sb.append(String.format("\nTiempo promedio: %d ns (%.3f ms)\n", 
                     averageTime, averageTime / 1_000_000.0));
            return sb.toString();
        }
    }

    public void cargar() {
        lista = new LinkedList<>();
        try {
            BufferedReader fb = new BufferedReader(new FileReader("data/data.txt"));
            String line = fb.readLine();
            while (line != null) {
                lista.add(Integer.parseInt(line));
                line = fb.readLine();
            }
            fb.close();
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
        }
    }

    /**
     * Método para agregar datos adicionales aleatorios usando Random
     * @param cantidad Número de datos aleatorios a agregar
     */
    public void agregarDatosAdicionales(int cantidad) {
        if (lista == null) {
            lista = new LinkedList<>();
        }
        
        Random random = new Random(); 
        
        System.out.println("Generando " + cantidad + " números aleatorios...");
        
        for (int i = 0; i < cantidad; i++) {
            int numeroAleatorio = random.nextInt(10000) + 1;
            lista.add(numeroAleatorio);
            
            if ((i + 1) % 1000 == 0) {
                System.out.printf("Generados %d/%d números...\n", i + 1, cantidad);
            }
        }
        
        System.out.println("✓ Se agregaron " + cantidad + " datos adicionales aleatorios.");
        System.out.println("✓ Total de elementos en la lista: " + lista.getLength());
    }

    /**
     * Método alternativo para generar datos en un rango específico
     * @param cantidad Número de datos a generar
     * @param min Valor mínimo (inclusive)
     * @param max Valor máximo (inclusive)
     */
    public void agregarDatosEnRango(int cantidad, int min, int max) {
        if (lista == null) {
            lista = new LinkedList<>();
        }
        
        Random random = new Random();
        
        System.out.printf("Generando %d números aleatorios entre %d y %d...\n", cantidad, min, max);
        
        for (int i = 0; i < cantidad; i++) {
            int numeroAleatorio = random.nextInt(max - min + 1) + min;
            lista.add(numeroAleatorio);
            
            if ((i + 1) % 1000 == 0) {
                System.out.printf("Generados %d/%d números...\n", i + 1, cantidad);
            }
        }
        
        System.out.println("✓ Se agregaron " + cantidad + " datos adicionales en rango [" + min + ", " + max + "].");
        System.out.println("✓ Total de elementos en la lista: " + lista.getLength());
    }

    //  MÉTODOS DE ORDENACIÓN 
    /**
     * QuickSort usando LinkedList personalizada con medición precisa
     */
    public SortingResult quickSortConMedicion(int repeticiones) {
        long[] tiempos = new long[repeticiones];
        
        for (int i = 0; i < repeticiones; i++) {
            cargar(); 
            
            if (!lista.isEmpty()) {
               
                Comparator<Integer> comparador = Integer::compareTo;
                
                long startTime = System.nanoTime();
                lista.quickSort(comparador);
                long endTime = System.nanoTime();
                
                tiempos[i] = endTime - startTime;
                
                System.out.printf("QuickSort ejecución %d: %d ns (%.3f ms)\n", 
                                i + 1, tiempos[i], tiempos[i] / 1_000_000.0);
            }
        }
        
        return new SortingResult("QuickSort", tiempos, lista.getLength());
    }

    /**
     * ShellSort usando LinkedList personalizada con medición precisa
     */
    public SortingResult shellSortConMedicion(int repeticiones) {
        long[] tiempos = new long[repeticiones];
        
        for (int i = 0; i < repeticiones; i++) {
            cargar(); 
            
            if (!lista.isEmpty()) {
                Comparator<Integer> comparador = Integer::compareTo;
                
                long startTime = System.nanoTime();
                lista.shellSort(comparador);
                long endTime = System.nanoTime();
                
                tiempos[i] = endTime - startTime;
                
                System.out.printf("ShellSort ejecución %d: %d ns (%.3f ms)\n", 
                                i + 1, tiempos[i], tiempos[i] / 1_000_000.0);
            }
        }
        
        return new SortingResult("ShellSort", tiempos, lista.getLength());
    }

    /**
     * Método para comparar ambos algoritmos
     */
    public void compararAlgoritmos(int repeticiones) {
        System.out.println("=".repeat(60));
        System.out.println("COMPARACIÓN DE ALGORITMOS DE ORDENACIÓN");
        System.out.println("=".repeat(60));
        
        System.out.println("\n--- QUICKSORT ---");
        SortingResult quickResult = quickSortConMedicion(repeticiones);
        
        System.out.println("\n--- SHELLSORT ---");
        SortingResult shellResult = shellSortConMedicion(repeticiones);
        
        mostrarTablaComparativa(quickResult, shellResult);
    }

    /**
     * Método para comparar con datos adicionales aleatorios
     */
    public void compararConDatosAdicionales(int repeticiones, int datosAdicionales) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("COMPARACIÓN CON DATOS ADICIONALES (" + datosAdicionales + " elementos extra)");
        System.out.println("=".repeat(70));
        
        cargar();
        agregarDatosAdicionales(datosAdicionales);
        
        LinkedList<Integer> listaQuick = lista.copy();
        LinkedList<Integer> listaShell = lista.copy();
        
        System.out.println("\nIniciando mediciones con " + lista.getLength() + " elementos...\n");
        
        System.out.println("--- QUICKSORT CON DATOS ADICIONALES ---");
        long[] tiemposQuick = new long[repeticiones];
        for (int i = 0; i < repeticiones; i++) {
            LinkedList<Integer> copia = listaQuick.copy();
            Comparator<Integer> comparador = Integer::compareTo;
            
            long startTime = System.nanoTime();
            copia.quickSort(comparador);
            long endTime = System.nanoTime();
            
            tiemposQuick[i] = endTime - startTime;
            System.out.printf("QuickSort (datos extra) ejecución %d: %d ns (%.3f ms)\n", 
                            i + 1, tiemposQuick[i], tiemposQuick[i] / 1_000_000.0);
        }
        
        System.out.println("\n--- SHELLSORT CON DATOS ADICIONALES ---");
        long[] tiemposShell = new long[repeticiones];
        for (int i = 0; i < repeticiones; i++) {
            LinkedList<Integer> copia = listaShell.copy();
            Comparator<Integer> comparador = Integer::compareTo;
            
            long startTime = System.nanoTime();
            copia.shellSort(comparador);
            long endTime = System.nanoTime();
            
            tiemposShell[i] = endTime - startTime;
            System.out.printf("ShellSort (datos extra) ejecución %d: %d ns (%.3f ms)\n", 
                            i + 1, tiemposShell[i], tiemposShell[i] / 1_000_000.0);
        }
        
        SortingResult quickResult = new SortingResult("QuickSort (Extra)", tiemposQuick, lista.getLength());
        SortingResult shellResult = new SortingResult("ShellSort (Extra)", tiemposShell, lista.getLength());
        
        mostrarTablaComparativa(quickResult, shellResult);
    }

    /**
     * Muestra tabla comparativa de resultados
     */
    private void mostrarTablaComparativa(SortingResult quick, SortingResult shell) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TABLA COMPARATIVA DE RESULTADOS");
        System.out.println("=".repeat(80));
        
        System.out.printf("%-25s %-15s %-20s %-20s\n", 
                         "Algoritmo", "Tamaño", "Tiempo Promedio (ns)", "Tiempo Promedio (ms)");
        System.out.println("-".repeat(80));
        
        System.out.printf("%-25s %-15d %-20d %-20.3f\n", 
                         quick.getAlgorithm(), quick.getDataSize(), 
                         quick.getAverageTime(), quick.getAverageTime() / 1_000_000.0);
        
        System.out.printf("%-25s %-15d %-20d %-20.3f\n", 
                         shell.getAlgorithm(), shell.getDataSize(), 
                         shell.getAverageTime(), shell.getAverageTime() / 1_000_000.0);
        
        System.out.println("-".repeat(80));
        
        if (quick.getAverageTime() < shell.getAverageTime()) {
            long diferencia = shell.getAverageTime() - quick.getAverageTime();
            double porcentaje = ((double) diferencia / shell.getAverageTime()) * 100;
            System.out.printf("✓ QuickSort es %.2f%% más rápido que ShellSort\n", porcentaje);
        } else {
            long diferencia = quick.getAverageTime() - shell.getAverageTime();
            double porcentaje = ((double) diferencia / quick.getAverageTime()) * 100;
            System.out.printf("✓ ShellSort es %.2f%% más rápido que QuickSort\n", porcentaje);
        }
        
        System.out.println("=".repeat(80));
    }

    private Boolean verificar_numero_arreglo(Integer a) {
        int cont = 0;
        Boolean band = false;
        for (int i = 0; i < matriz.length; i++) {
            if (a.intValue() == matriz[i].intValue())
                cont++;
            if (cont >= 2) {
                band = true;
                break;
            }
        }
        return band;
    }

    public String[] verificar_arreglo() {
        StringBuilder resp = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            if (verificar_numero_arreglo(matriz[i]))
                resp.append(matriz[i].toString()).append("-");
        }
        return resp.toString().split("-");
    }

    private Boolean verificar_numero_lista(Integer a) {
        int cont = 0;
        Boolean band = false;
        for (int i = 0; i < lista.getLength(); i++) {
            if (a.intValue() == lista.get(i).intValue())
                cont++;
            if (cont >= 2) {
                band = true;
                break;
            }
        }
        return band;
    }

    public LinkedList<Integer> verificar_lista() {
        LinkedList<Integer> resp = new LinkedList<>();
        for (int i = 0; i < lista.getLength(); i++) {
            if (verificar_numero_lista(lista.get(i).intValue()))
                resp.add(lista.get(i));
        }
        return resp;
    }

    public LinkedList<Integer> burbuja() {
        cargar();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();

        if (!lista.isEmpty()) {
            Integer[] m = lista.toArray();
            for (int i = 0; i < m.length - 1; i++) {
                for (int j = 0; j < (m.length - 1 - i); j++) {
                    if (m[j] > m[j + 1]) {
                        Integer aux = m[j];
                        m[j] = m[j + 1];
                        m[j + 1] = aux;
                        cont++;
                    }
                }
            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(m);
        }
        return lista;
    }

    public LinkedList<Integer> burbujaMejorado() {
        cargar();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();

        if (!lista.isEmpty()) {
            Integer arreglo[] = lista.toArray();
            int intercambios = 0, comparaciones = 0;
            int i, izq, der, k, aux;
            izq = 1;
            der = arreglo.length - 1;
            k = arreglo.length - 1;
            while (izq <= der) {
                for (i = der; i >= izq; i--) {
                    comparaciones++;
                    if (arreglo[i - 1] > arreglo[i]) {
                        intercambios++;
                        aux = arreglo[i - 1];
                        arreglo[i - 1] = arreglo[i];
                        arreglo[i] = aux;
                        k = i;
                    }
                }
                izq = k + 1;
                for (i = izq; i <= der; i++) {
                    comparaciones++;
                    if (arreglo[i - 1] > arreglo[i]) {
                        intercambios++;
                        aux = arreglo[i - 1];
                        arreglo[i - 1] = arreglo[i];
                        arreglo[i] = aux;
                        k = i;
                    }
                }
                der = k - 1;
            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + intercambios);
            lista.toList(arreglo);
        }
        return lista;
    }

    public LinkedList<Integer> insercion() {
        cargar();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();
        Integer array[] = lista.toArray();
        if (!lista.isEmpty()) {
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j = j - 1;
                    cont++;
                }
                array[j + 1] = key;
            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(array);
        }
        return lista;
    }

    public LinkedList<Integer> seleccion(Integer type) {
        cargar();
        if (!lista.isEmpty()) {
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            Integer arr[] = lista.toArray();
            int n = arr.length;
            if (type == 1) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] < arr[min_idx]) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    int temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] > arr[min_idx]) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    int temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(arr);
        }
        return lista;
    }

    public void q_order() {
        cargar();
        if (!lista.isEmpty()) {
            Comparator<Integer> comparador = Integer::compareTo;
            long startTime = System.nanoTime();
            lista.quickSort(comparador);
            long endTime = System.nanoTime() - startTime;
            System.out.println("QuickSort se ha demorado " + endTime + " ns (" + 
                             (endTime / 1_000_000.0) + " ms)");
        }        
    }

    public void s_order() {
        cargar();
        if (!lista.isEmpty()) {
            Comparator<Integer> comparador = Integer::compareTo;
            long startTime = System.nanoTime();
            lista.shellSort(comparador);
            long endTime = System.nanoTime() - startTime;
            System.out.println("ShellSort se ha demorado " + endTime + " ns (" + 
                             (endTime / 1_000_000.0) + " ms)");
        }        
    }

    public static void main(String[] args) {
        Practica p = new Practica();
        
        System.out.println("PRACTICA DE ALGORITMOS DE ORDENACION");
        System.out.println("====================================");
        
        p.compararAlgoritmos(3);
        
        System.out.println("\n  PRUEBA CON DATOS ALEATORIOS");
        p.compararConDatosAdicionales(3, 10000);
        
        System.out.println("\n--- Métodos individuales (SU COMPATIBILIDAD) ---");
        p.q_order();
        p.s_order();
        
        System.out.println("\n PRÁCTICA SE HIZO EXITOSAMENTE");
    }
}