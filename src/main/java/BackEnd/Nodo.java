package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Nodo genérico de la lista circular doblemente enlazada.
 * En una lista circular nunca hay null en siguiente/anterior mientras
 * el nodo pertenece a la lista: el último apunta al primero y viceversa.
 */
public class Nodo<T extends Trabajo> {

    private final T dato;      // el trabajo (Libro, Revista o Articulo)
    private Nodo<T> siguiente; // enlace al nodo que sigue en el ciclo
    private Nodo<T> anterior;  // enlace al nodo previo en el ciclo

    public Nodo(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo<T> anterior) {
        this.anterior = anterior;
    }
}
