package BackEnd;

import java.util.ArrayList;
import java.util.List;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Lista Circular Doblemente Enlazada genérica para trabajos de la biblioteca.
 *
 *   cabeza ⇄ n1 ⇄ n2 ⇄ ... ⇄ último ⇄ (de vuelta a cabeza)
 *
 * - cabeza.getAnterior() es siempre el último nodo (cierre del ciclo).
 * - actual es el cursor de navegación (siguiente / anterior).
 * - Las posiciones empiezan en 0.
 */
public class ListaCircularTrabajos<T extends Trabajo> {

    private Nodo<T> cabeza;
    private Nodo<T> actual;
    private int tamanio;

    public ListaCircularTrabajos() {
        vaciarLista();
    }

    // ===================== consultas básicas =====================

    public boolean isEmpty() {
        return tamanio == 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    // ===================== inserción =====================

    public void insertarFinal(T trabajo) {
        Nodo<T> nuevo = crearNodo(trabajo);
        if (isEmpty()) {
            // Un solo nodo: se apunta a sí mismo en ambas direcciones
            nuevo.setSiguiente(nuevo);
            nuevo.setAnterior(nuevo);
            cabeza = nuevo;
            actual = nuevo;
        } else {
            Nodo<T> ultimo = cabeza.getAnterior();
            enlazarEntre(ultimo, nuevo, cabeza);
        }
        tamanio++;
    }

    public void insertarInicio(T trabajo) {
        // En un ciclo, "antes de la cabeza" es lo mismo que "después del último":
        // se inserta al final y la cabeza retrocede un lugar hacia el nuevo nodo.
        insertarFinal(trabajo);
        cabeza = cabeza.getAnterior();
    }

    /** Inserta en la posición indicada (0 ≤ posicion ≤ tamaño). */
    public void insertarPosicion(T trabajo, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición de inserción inválida: " + posicion
                    + ". Debe estar entre 0 y " + tamanio + ".");
        }
        if (posicion == 0) {
            insertarInicio(trabajo);
        } else if (posicion == tamanio) {
            insertarFinal(trabajo);
        } else {
            Nodo<T> nuevo = crearNodo(trabajo);
            Nodo<T> ocupante = nodoEn(posicion);
            enlazarEntre(ocupante.getAnterior(), nuevo, ocupante);
            tamanio++;
        }
    }

    // ===================== eliminación =====================

    /** Elimina el nodo de la posición indicada (0 ≤ posicion < tamaño) y devuelve su trabajo. */
    public T eliminar(int posicion) {
        verificarNoVacia("eliminar");
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición de eliminación inválida: " + posicion
                    + ". Debe estar entre 0 y " + (tamanio - 1) + ".");
        }
        return desenlazar(nodoEn(posicion));
    }

    /** Elimina el primer trabajo cuyo título coincida (sin importar mayúsculas). Devuelve null si no existe. */
    public T eliminarPorTitulo(String titulo) {
        verificarNoVacia("eliminar");
        Nodo<T> encontrado = buscarNodo(titulo);
        return encontrado == null ? null : desenlazar(encontrado);
    }

    /** Elimina todos los nodos y deja la estructura como recién creada. */
    public final void vaciarLista() {
        if (cabeza != null) {
            // Rompemos los enlaces para no dejar el ciclo vivo en memoria
            Nodo<T> recorrido = cabeza;
            for (int i = 0; i < tamanio; i++) {
                Nodo<T> sig = recorrido.getSiguiente();
                recorrido.setSiguiente(null);
                recorrido.setAnterior(null);
                recorrido = sig;
            }
        }
        cabeza = null;
        actual = null;
        tamanio = 0;
    }

    // ===================== navegación cíclica =====================

    public T siguiente() {
        verificarNoVacia("navegar");
        actual = actual.getSiguiente();
        return actual.getDato();
    }

    public T anterior() {
        verificarNoVacia("navegar");
        actual = actual.getAnterior();
        return actual.getDato();
    }

    public T consultarActual() {
        verificarNoVacia("consultar");
        return actual.getDato();
    }

    /** Posición del cursor actual (−1 si la lista está vacía). */
    public int getIndiceActual() {
        if (isEmpty()) {
            return -1;
        }
        Nodo<T> recorrido = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (recorrido == actual) {
                return i;
            }
            recorrido = recorrido.getSiguiente();
        }
        return -1;
    }

    // ===================== búsqueda y recorrido =====================

    /** Devuelve el trabajo con ese título o null si no existe. */
    public T buscarPorTitulo(String titulo) {
        Nodo<T> nodo = buscarNodo(titulo);
        return nodo == null ? null : nodo.getDato();
    }

    /** Posición del trabajo con ese título o −1 si no existe. */
    public int indiceDe(String titulo) {
        Nodo<T> nodo = buscarNodo(titulo);
        if (nodo == null) {
            return -1;
        }
        Nodo<T> recorrido = cabeza;
        int i = 0;
        while (recorrido != nodo) {
            recorrido = recorrido.getSiguiente();
            i++;
        }
        return i;
    }

    /** Recorre exactamente una vuelta, desde la cabeza hasta volver a ella. */
    public String mostrarTodos() {
        if (isEmpty()) {
            return "La lista circular está vacía (cabeza = null).";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Nodos: ").append(tamanio).append("\n\n");
        Nodo<T> recorrido = cabeza;
        int i = 0;
        do {
            sb.append("[").append(i).append("]");
            if (recorrido == cabeza) {
                sb.append(" «CABEZA»");
            }
            if (recorrido == actual) {
                sb.append(" ► ACTUAL");
            }
            sb.append("\n    ").append(recorrido.getDato()).append("\n");
            sb.append("      ⇅\n");
            recorrido = recorrido.getSiguiente();
            i++;
        } while (recorrido != cabeza);
        sb.append("↺ el último nodo enlaza de vuelta a la CABEZA: \"")
          .append(cabeza.getDato().getTitulo()).append("\"");
        return sb.toString();
    }

    /** Copia de los trabajos en orden (una vuelta), útil para dibujar la lista. */
    public List<T> aLista() {
        List<T> resultado = new ArrayList<>();
        if (isEmpty()) {
            return resultado;
        }
        Nodo<T> recorrido = cabeza;
        do {
            resultado.add(recorrido.getDato());
            recorrido = recorrido.getSiguiente();
        } while (recorrido != cabeza);
        return resultado;
    }

    /**
     * Comprueba la integridad del ciclo: avanzar tamaño pasos hacia adelante
     * y hacia atrás desde la cabeza debe regresar a la cabeza, y cada enlace
     * siguiente/anterior debe ser recíproco.
     */
    public boolean esCircularValida() {
        if (isEmpty()) {
            return cabeza == null && actual == null;
        }
        Nodo<T> adelante = cabeza;
        Nodo<T> atras = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (adelante.getSiguiente().getAnterior() != adelante) {
                return false;
            }
            adelante = adelante.getSiguiente();
            atras = atras.getAnterior();
        }
        return adelante == cabeza && atras == cabeza && getIndiceActual() >= 0;
    }

    // ===================== auxiliares privados =====================

    private Nodo<T> crearNodo(T trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException("No se puede insertar un trabajo nulo.");
        }
        return new Nodo<>(trabajo);
    }

    /** Coloca "nuevo" entre "antes" y "despues". */
    private void enlazarEntre(Nodo<T> antes, Nodo<T> nuevo, Nodo<T> despues) {
        nuevo.setAnterior(antes);
        nuevo.setSiguiente(despues);
        antes.setSiguiente(nuevo);
        despues.setAnterior(nuevo);
    }

    /** Quita el nodo del ciclo uniendo su anterior con su siguiente. */
    private T desenlazar(Nodo<T> nodo) {
        T dato = nodo.getDato();
        if (tamanio == 1) {
            cabeza = null;
            actual = null;
        } else {
            nodo.getAnterior().setSiguiente(nodo.getSiguiente());
            nodo.getSiguiente().setAnterior(nodo.getAnterior());
            if (nodo == cabeza) {
                cabeza = nodo.getSiguiente();
            }
            if (nodo == actual) {
                actual = nodo.getSiguiente();
            }
        }
        nodo.setSiguiente(null);
        nodo.setAnterior(null);
        tamanio--;
        return dato;
    }

    /** Llega a la posición por el camino más corto (adelante o atrás). */
    private Nodo<T> nodoEn(int posicion) {
        Nodo<T> recorrido = cabeza;
        if (posicion <= tamanio / 2) {
            for (int i = 0; i < posicion; i++) {
                recorrido = recorrido.getSiguiente();
            }
        } else {
            for (int i = tamanio; i > posicion; i--) {
                recorrido = recorrido.getAnterior();
            }
        }
        return recorrido;
    }

    private Nodo<T> buscarNodo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título a buscar no puede estar vacío.");
        }
        if (isEmpty()) {
            return null;
        }
        String buscado = titulo.trim();
        Nodo<T> recorrido = cabeza;
        do {
            if (recorrido.getDato().getTitulo().equalsIgnoreCase(buscado)) {
                return recorrido;
            }
            recorrido = recorrido.getSiguiente();
        } while (recorrido != cabeza);
        return null;
    }

    private void verificarNoVacia(String operacion) {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "No se puede " + operacion + ": la lista circular está vacía.");
        }
    }
}
