import java.io.*;
import java.util.*;

public class Controlador {

    public static void main(String[] args) {
        String archivoPath = "data.txt";

        LinkedList<String> dataList = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataList.addLast(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error");
            return;
        }

        String[] dataArray = new String[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }

        Set<String> vistos = new HashSet<>();
        Set<String> iguales = new HashSet<>();
        for (String dato : dataArray) {
            if (!vistos.add(dato)) {
                iguales.add(dato);
            }
        }

        System.out.println("Elementos iguales:");
        for (String s : iguales) {
            System.out.println(s);
        }
        System.out.println("Total: " + iguales.size());

        long inicio = System.currentTimeMillis();
        compararRendimiento(dataArray, dataList, 3);
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecucion: " + (fin - inicio) + " ms");
    }

    private static void compararRendimiento(String[] dataArray, LinkedList<String> dataList, int numRepeticiones) {
        System.out.println("Tiempo en ms:");
        System.out.println("| tiempos | Arreglo | Lista |");

        for (int i = 1; i <= numRepeticiones; i++) {
            long tiempoArreglo = medirTiempoArreglo(dataArray);
            long tiempoLista = medirTiempoLista(dataList);
            System.out.printf("| %9d | %7d | %5d |\n", i, tiempoArreglo, tiempoLista);
        }
    }

    private static long medirTiempoArreglo(String[] dataArray) {
        long inicio = System.currentTimeMillis();
        for (String item : dataArray) {
            String temp = item;
        }
        long fin = System.currentTimeMillis();
        return fin - inicio;
    }

    private static long medirTiempoLista(LinkedList<String> dataList) {
        long inicio = System.currentTimeMillis();
        for (String item : dataList) {
            String temp = item;
        }
        long fin = System.currentTimeMillis();
        return fin - inicio;
    }
}
