package Pruebas;

import BackEnd.Articulo;
import BackEnd.Libro;
import BackEnd.ListaCircularTrabajos;
import BackEnd.Revista;
import BackEnd.Trabajo;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Pruebas de funcionamiento (punto 6) ejecutables por consola.
 * Clic derecho sobre el archivo → Run File.
 */
public class PruebasListaCircular {

    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        ListaCircularTrabajos<Trabajo> lista = new ListaCircularTrabajos<>();

        System.out.println("== 1. Lista recién creada ==");
        comprobar("isEmpty() es true al inicio", lista.isEmpty());
        comprobar("siguiente() en vacía lanza excepción", lanza(() -> lista.siguiente()));
        comprobar("anterior() en vacía lanza excepción", lanza(() -> lista.anterior()));
        comprobar("consultarActual() en vacía lanza excepción", lanza(() -> lista.consultarActual()));
        comprobar("eliminar(0) en vacía lanza excepción", lanza(() -> lista.eliminar(0)));

        System.out.println("\n== 2. Inserciones de distintos tipos y posiciones ==");
        lista.insertarFinal(new Libro("Cien años de soledad", "G. García Márquez", 1967, 471));
        lista.insertarFinal(new Revista("National Geographic", "NatGeo Society", 2024, 312));
        lista.insertarInicio(new Articulo("Redes Neuronales", "Y. LeCun", 1998, "IEEE"));
        lista.insertarPosicion(new Libro("Rayuela", "J. Cortázar", 1963, 600), 2);
        lista.insertarPosicion(new Revista("Muy Interesante", "Varios", 2023, 500), 4);
        // Orden esperado: Redes, Cien años, Rayuela, NatGeo, Muy Interesante
        comprobar("tamaño = 5", lista.getTamanio() == 5);
        comprobar("orden correcto", titulos(lista).equals(
                "Redes Neuronales, Cien años de soledad, Rayuela, National Geographic, Muy Interesante"));
        comprobar("posición -1 rechazada", lanza(() -> lista.insertarPosicion(new Libro("X", "Y", 2000, 1), -1)));
        comprobar("posición > tamaño rechazada", lanza(() -> lista.insertarPosicion(new Libro("X", "Y", 2000, 1), 6)));
        comprobar("trabajo con año 0 rechazado", lanza(() -> new Libro("X", "Y", 0, 10)));
        comprobar("título vacío rechazado", lanza(() -> new Revista("  ", "Y", 2000, 1)));
        comprobar("ciclo íntegro", lista.esCircularValida());
        System.out.println(lista.mostrarTodos());

        System.out.println("\n== 3. Navegación cíclica ==");
        // El cursor quedó en el primer trabajo insertado (Cien años, posición 1)
        comprobar("cursor inicial en posición 1", lista.getIndiceActual() == 1);
        for (int i = 0; i < 3; i++) {
            lista.siguiente();
        }
        comprobar("3 × siguiente → último (pos 4)", lista.getIndiceActual() == 4);
        lista.siguiente();
        comprobar("siguiente desde el último → cabeza (pos 0)", lista.getIndiceActual() == 0);
        lista.anterior();
        comprobar("anterior desde la cabeza → último (pos 4)", lista.getIndiceActual() == 4);
        for (int i = 0; i < 5; i++) {
            lista.siguiente();
        }
        comprobar("5 × siguiente = vuelta completa", lista.getIndiceActual() == 4);

        System.out.println("\n== 4. Eliminaciones conservando la circularidad ==");
        comprobar("eliminar cabeza (0)", lista.eliminar(0).getTitulo().equals("Redes Neuronales"));
        comprobar("ciclo íntegro tras eliminar cabeza", lista.esCircularValida());
        comprobar("eliminar intermedio por título",
                lista.eliminarPorTitulo("rayuela") != null);
        comprobar("ciclo íntegro tras eliminar intermedio", lista.esCircularValida());
        // El cursor estaba en el último (Muy Interesante); al borrarlo pasa al siguiente = cabeza
        comprobar("eliminar último", lista.eliminar(lista.getTamanio() - 1).getTitulo().equals("Muy Interesante"));
        comprobar("cursor movido a la cabeza", lista.getIndiceActual() == 0);
        comprobar("ciclo íntegro tras eliminar último", lista.esCircularValida());
        comprobar("título inexistente devuelve null", lista.eliminarPorTitulo("No existe") == null);
        comprobar("eliminar fuera de rango rechazado", lanza(() -> lista.eliminar(2)));
        comprobar("quedan 2 nodos", titulos(lista).equals("Cien años de soledad, National Geographic"));

        System.out.println("\n== 5. toString() polimórfico ==");
        for (Trabajo t : lista.aLista()) {
            System.out.println("  " + t);
        }

        System.out.println("\n== 6. Vaciar ==");
        lista.vaciarLista();
        comprobar("isEmpty() es true tras vaciar", lista.isEmpty());
        comprobar("tamaño = 0", lista.getTamanio() == 0);
        comprobar("navegar tras vaciar lanza excepción", lanza(() -> lista.siguiente()));
        lista.insertarFinal(new Libro("Único", "Autor", 2020, 10));
        comprobar("un solo nodo se enlaza consigo mismo",
                lista.siguiente() == lista.anterior() && lista.esCircularValida());

        System.out.println("\nResultado: " + aprobadas + " aprobadas, " + fallidas + " fallidas.");
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static String titulos(ListaCircularTrabajos<Trabajo> lista) {
        StringBuilder sb = new StringBuilder();
        for (Trabajo t : lista.aLista()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(t.getTitulo());
        }
        return sb.toString();
    }

    private static boolean lanza(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }

    private static void comprobar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("  [OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }
}
